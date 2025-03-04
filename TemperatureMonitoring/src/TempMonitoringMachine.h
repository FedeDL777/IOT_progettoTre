#ifndef __TMM__
#define __TMM__

#include <Arduino.h>
#include "include/Led.h"
#include "include/SensorTemp.h"
#include "include/set_up.h"

class TMMSystem
{
public:
    TMMSystem();

    float getTemperature();
    void normal();
    void problem();

private:
    float temperature;
    Led *redLed;
    Led *greenLed;
    SensorTemp *sensor;
};

#endif
