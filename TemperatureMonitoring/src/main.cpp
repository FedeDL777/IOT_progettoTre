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

enum State
{
    NOT_CONNECTED,
    CONNECTED
} state;

MQTTConnection mqttConnection(WIFI_SSID, WIFI_PASSWORD, MQTT_SERVER, TOPIC);
TMMSystem *machine;
void setup()
{
    Serial.begin(115200);
    machine = new TMMSystem();
    mqttConnection.setup();
    state = CONNECTED;
}

void loop()
{
    if (state == CONNECTED)
    {
        if (!mqttConnection.isConnected())
        {
            machine->problem();
            state = NOT_CONNECTED;
        }
        else
        {
            mqttConnection.loop();
            float temperature = machine->getTemperature();
            if (temperature < 20)
            {
                machine->normal();
            }
            else
            {
                machine->problem();
            }
    
            if (mqttConnection.publish(String(temperature).c_str()))
            {
                Serial.println("Message sent!");
            }
            else
            {
                Serial.println("Error sending message!");
            }
        }
        

    }
    else
    {
        Serial.println("Not connected!");
    }

    
}
