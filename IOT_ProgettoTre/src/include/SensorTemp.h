#ifndef __TEMPSENS__
#define __TEMPSENS

#define VCC ((float)5)
class SensorTemp{
    public:
        SensorTemp(int pin);
        float getTemperature();
    private:
        int pin;

};

#endif