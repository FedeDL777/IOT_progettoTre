#include <WiFi.h>
#include <HTTPClient.h>
#include "include/set_up.h"
#include "TempMonitoringMachine.h"
#include "MQTTConnection.h"

#define NORMAL_SAMPLE 5000

unsigned long lastSample;
unsigned long checkSample;

enum NetworkState
{
    NOT_CONNECTED,
    CONNECTED
} networkState;

MQTTConnection mqttConnection(WIFI_SSID, WIFI_PASSWORD, MQTT_SERVER, (const char *[]){TOPIC_PER, TOPIC_TEMP}, 2);
TMMSystem *machine;

void setup()
{
    Serial.begin(115200);
    machine = new TMMSystem();
    mqttConnection.setup();

    // Verifica connessione iniziale
    networkState = (WiFi.status() == WL_CONNECTED) ? CONNECTED : NOT_CONNECTED;
    lastSample = millis();
    checkSample = NORMAL_SAMPLE;
}

void loop()
{
    if (networkState == CONNECTED)
    {
        if (!mqttConnection.isConnected())
        {
            machine->problem();
            networkState = NOT_CONNECTED;
        }
        else
        {
            mqttConnection.loop();
            if (millis() - lastSample >= checkSample)
            {
                lastSample = millis();
                checkSample = mqttConnection.getSamplingTime();

                float temperature = machine->getTemperature();
                char temperatureStr[10];
                dtostrf(temperature, 6, 2, temperatureStr);

                Serial.println("Publishing: " + String(temperature));
                mqttConnection.publish(TOPIC_TEMP, temperatureStr);
            }
        }
    }
    else
    {
        mqttConnection.reconnect();
        if (mqttConnection.isConnected())
        {
            machine->normal();
            networkState = CONNECTED;
        }
    }
}

