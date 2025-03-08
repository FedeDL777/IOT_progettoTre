#include "TempMonitoringMachine.h"

TMMSystem::TMMSystem()
{
    this->redLed = new Led(LED_RED_PIN);
    this->greenLed = new Led(LED_GREEN_PIN);
    this->sensor = new SensorTemp(TEMPERATURE_SENSOR_PIN);
    this->temperature = 0;
    this->samplingTime = 5000;
}

float TMMSystem::getTemperature()
{
    this->temperature = this->sensor->getTemperature();
    return this->temperature;
}

void TMMSystem::normal()
{
    this->greenLed->switchOn();
    this->redLed->switchOff();
}

void TMMSystem::problem()
{
    this->greenLed->switchOff();
    this->redLed->switchOn();
}

int TMMSystem::getSamplingTime()
{
    return this->samplingTime;
    
    
}

void TMMSystem::setSamplingTime(int samplingTime)
{
    this->samplingTime = samplingTime;
}
