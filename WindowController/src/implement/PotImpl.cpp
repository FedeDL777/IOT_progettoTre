#include "../include/Pot.h"
#define MIN_DEGREE 0
#define MAX_DEGREE 90
Pot::Pot(int pin)
{
    this->pin = pin;
    pinMode(pin, INPUT);
}

int Pot::getDegree()
{
    readValue();
    return this->degree;
}
double Pot::myMap(double x, double in_min, double in_max, double out_min, double out_max)
{
    return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
}

void Pot::readValue()
{
    int rawValue = analogRead(this->pin);
    rawValue = constrain(rawValue, 0, 1023); // Limita i valori a 0-1023
    this->degree = map(rawValue, 0, 1023, MIN_DEGREE, MAX_DEGREE);
}