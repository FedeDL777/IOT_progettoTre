#ifndef MQTTCONNECTION_
#define MQTTCONNECTION_

#include <WiFi.h>
#include <PubSubClient.h>

class MQTTConnection
{
private:
    WiFiClient espClient;
    PubSubClient client;
    const char *ssid;
    const char *password;
    const char *mqtt_server;
    const char *topic;

    void connectWiFi();
    void connectMQTT();

public:
    MQTTConnection(const char *ssid, const char *password, const char *mqtt_server, const char *topic);

    void setup();
    void loop();
    bool publish(const char *message);
    void callback(char *topic, byte *payload, unsigned int length);
};

#endif
