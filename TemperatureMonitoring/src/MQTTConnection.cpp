#include <WiFi.h>
#include <PubSubClient.h>
#include "MQTTConnection.h"
#include <HTTPClient.h>

MQTTConnection::MQTTConnection(const char *ssid, const char *password, const char *mqtt_server, const char *topic)
{
}

void MQTTConnection::setup() {
    delay(10);
    Serial.println(String("Connecting to ") + ssid);
    WiFi.mode(WIFI_STA);
    WiFi.begin(ssid, password);
    while (WiFi.status() != WL_CONNECTED) {
      delay(500);
      Serial.print(".");
    }
    Serial.println("");
    Serial.println("WiFi connected");
    Serial.println("IP address: ");
    Serial.println(WiFi.localIP());
}

void MQTTConnection::reconnect() {
    while (!client.connected()) {
        Serial.print("Connessione a MQTT...");
        if (client.connect("ESP32_Client")) {
            Serial.println("Connesso!");
        } else {
            Serial.print("Fallito, rc=");
            Serial.print(client.state());
            Serial.println(" Riprovo tra 5 secondi...");
            delay(5000);
        }
    }
}

bool MQTTConnection::publish(const char *message)
{        
       return this->client.publish(topic, message);
     
}

void MQTTConnection::loop() {
    if (!client.connected()) {
        reconnect();
    }
    this->client.loop();
}

void MQTTConnection::callback(char* topic, byte* payload, unsigned int length) {
    Serial.println(String("Message arrived on [") + topic + "] len: " + length );
  }



void MQTTConnection::reconnect() {
  
    // Loop until we're reconnected
    
    while (!client.connected()) {
      Serial.print("Attempting MQTT connection...");
      
      // Create a random client ID
      String clientId = String("IOT-Progetto-03-client")+String(random(0xffff), HEX);
  
      // Attempt to connect
      if (client.connect(clientId.c_str())) {
        Serial.println("connected");
        // Once connected, publish an announcement...
        // client.publish("outTopic", "hello world");
        // ... and resubscribe
        client.subscribe(topic);
      } else {
        Serial.print("failed, rc=");
        Serial.print(client.state());
        Serial.println(" try again in 5 seconds");
        // Wait 5 seconds before retrying
        delay(5000);
      }
    }
  }