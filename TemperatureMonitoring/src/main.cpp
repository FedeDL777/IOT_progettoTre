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
    NOT_COTTECTED,
    CONNECTED
} state;

MQTTConnection mqttConnection(WIFI_SSID, WIFI_PASSWORD, MQTT_SERVER, TOPIC);
TMMSystem *machine;
void setup()
{
    Serial.begin(115200);
    machine = new TMMSystem();
    mqttConnection.setup();
}

void loop()
{
}
