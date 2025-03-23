#ifndef MQTTCONNECTION_
#define MQTTCONNECTION_

#include <WiFi.h>
#include <PubSubClient.h>
#include "include/set_up.h"

class MQTTConnection
{
private:
    WiFiClient espClient;
    PubSubClient client;
    const char *ssid;
    const char *password;
    const char *mqtt_server;
    const char **topics;
    int numTopics;
    int samplingTime;
    void connectWiFi();
    void connectMQTT();

public:
    MQTTConnection(const char *ssid, const char *password, const char *mqtt_server, const char **topics, int numTopics);
    int getSamplingTime();
    void setup();
    void loop();
    void reconnect();
    bool isConnected();
    bool publish(const char *topic, const char *message);
    void callback(char *topic, byte *payload, unsigned int length);
};

#endif
