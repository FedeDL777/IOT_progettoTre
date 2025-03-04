#ifndef CONFIG_H
#define CONFIG_H

#include <Arduino.h>
/* Hardware configs */
#define LED_RED_PIN 32
#define LED_GREEN_PIN 33
#define TEMPERATURE_SENSOR_PIN 35

/* Network */
#define WIFI_SSID "*"
#define WIFI_PASSWORD "ciao"
#define MQTT_SERVER "broker.mqtt-dashboard.com" //
#define TOPIC "IOT-Progetto-03"

#endif