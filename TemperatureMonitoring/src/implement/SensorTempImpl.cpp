#include "../include/SensorTemp.h"
#include "Arduino.h"

SensorTemp::SensorTemp(int pin)
{
  this->pin = pin;
}

float SensorTemp::getTemperature()
{
  Serial.println(analogRead(pin));
  int valoreAnalogico = analogRead(pin); // Legge il valore ADC (0-4095)
    
  // Converti la lettura in tensione
  float valoreInVolt = (float)valoreAnalogico * VCC / 4095.0;
  
  // Converti la tensione in temperatura (10mV per °C → 0.01V per °C)
  float temperaturaC = valoreInVolt / 0.01;

  return temperaturaC;
}
