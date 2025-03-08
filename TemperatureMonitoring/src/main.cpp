/*
 * HTTPClient lib --  Performing an HTTP POST to our REST service
 *
 * Remark:
 * - Going through ngrok
 *
 */
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

MQTTConnection mqttConnection(WIFI_SSID, WIFI_PASSWORD, MQTT_SERVER, TOPIC);
TMMSystem *machine;
void setup()
{
    Serial.begin(115200);
    machine = new TMMSystem();
    mqttConnection.setup();
    networkState = CONNECTED;
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
            if (millis() > lastSample + checkSample)
            {
                lastSample = millis();
                checkSample = mqttConnection.getSamplingTime();
                float temperature = machine->getTemperature();
                Serial.println("Publishing: " + String(temperature)); /// guardaci
                mqttConnection.publish(("ts:" + String(temperature)).c_str());
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
