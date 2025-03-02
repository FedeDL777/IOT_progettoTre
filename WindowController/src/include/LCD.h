#ifndef __LCD__
#define __LCD__
#include <LiquidCrystal_I2C.h>
#include "set_up.h"
class LCD
{
public:
    LCD();
    void setup();

    void turnOff();
    void turnOn();
    void displayManual(int t, int d);
    void displayAutomatic(int t, int d);
    void displayProblem();

private:
    int mapOpening(int d);
    LiquidCrystal_I2C *pLcd;
    void initCursor();
};

#endif