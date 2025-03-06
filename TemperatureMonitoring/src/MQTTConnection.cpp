#include <WiFi.h>
#include <PubSubClient.h>
#include "MQTTConnection.h"

MQTTConnection::MQTTConnection(const char *ssid, const char *password, const char *mqtt_server, const char *topic)
    : ssid(ssid), password(password), mqtt_server(mqtt_server), topic(topic), client(espClient) {}

void MQTTConnection::connectWiFi()
{
    Serial.println(String("Connecting to ") + ssid);
    WiFi.mode(WIFI_STA);
    WiFi.begin(ssid, password);

    unsigned long startAttemptTime = millis();
    while (WiFi.status() != WL_CONNECTED && millis() - startAttemptTime < 20000)
    {
        delay(500);
        Serial.print(".");
    }

    if (WiFi.status() == WL_CONNECTED)
    {
        Serial.println("\nWiFi connected, IP: " + WiFi.localIP().toString());
    }
    else
    {
        Serial.println("\nWiFi connection failed!");
    }
}

void MQTTConnection::connectMQTT()
{
    while (!client.connected())
    {
        Serial.print("Attempting MQTT connection... ");
        String clientId = String("IOT-Progetto-03-client-") + String(random(0xFFFF), HEX);

        if (client.connect(clientId.c_str()))
        {
            Serial.println("Connected!");
            client.subscribe(topic);
        }
        else
        {
            Serial.print("Failed, rc=");
            Serial.print(client.state());
            Serial.println(" retrying in 5s...");
            delay(5000);
        }
    }
}

void MQTTConnection::setup()
{
    connectWiFi();
    client.setServer(mqtt_server, 1883);
    connectMQTT();
}

void MQTTConnection::loop()
{
    client.loop();
}

void MQTTConnection::reconnect()
{
        if (!client.connected())
        {
            connectMQTT();
        }
}

bool MQTTConnection::isConnected()
{
    return this->client.connected();
}

bool MQTTConnection::publish(const char *message)
{
    if (client.connected())
    {
        return client.publish(topic, message);
    }
    return false;
}

void MQTTConnection::callback(char *topic, byte *payload, unsigned int length)
{
    Serial.println(String("Message arrived on [") + topic + "] len: " + length);
}
