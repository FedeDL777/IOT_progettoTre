#include "../include/SensorTemp.h"	
#include "Arduino.h"

SensorTemp::SensorTemp(int pin)
{
    this->pin = pin;
}

float SensorTemp::getTemperature()
{
  int valoreAnalogico = analogRead(pin);  // Legge il valore dal sensore
  float valoreInVolt = valoreAnalogico*VCC/1023;  
  return valoreInVolt/0.01;
}
