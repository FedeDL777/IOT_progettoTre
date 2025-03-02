#include "set_up.h"

#ifndef __POT__
#define __POT__

class Pot
{
private:
    int pin;
    int degree;
    void readValue();
    double myMap(double x, double in_min, double in_max, double out_min, double out_max);
public:
    Pot(int pin);
    int getDegree();
};


#endif