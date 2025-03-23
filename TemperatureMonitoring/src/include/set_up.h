#ifndef CONFIG_H
#define CONFIG_H

#include <Arduino.h>
/* Hardware configs */
#define LED_RED_PIN 32
#define LED_GREEN_PIN 33
#define TEMPERATURE_SENSOR_PIN 34

/* Network */
#define WIFI_SSID "Home&Life SuperWiFi-6985"
#define WIFI_PASSWORD "Tamburini78"
#define MQTT_SERVER "broker.mqtt-dashboard.com" //
#define TOPIC_PER "IOT-Progetto-03-period"
#define TOPIC_TEMP "IOT-Progetto-03-temp"

#endif