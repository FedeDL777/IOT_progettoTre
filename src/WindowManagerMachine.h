#ifndef __WMM__
#define __WMM__

#include <Arduino.h>
#include "include/Button.h"
#include "include/Led.h"
#include "include/MotorImpl.h"
#include "include/LCD.h"
#define OPEN 90
#define CLOSE 0

class WMMSystem
{
public:
    WMMSystem();
    void init();

    void switchState();
    bool isManual();
    bool isNormal();

    void fullyOpenServo();
    void closeServo();
    void openServo(int degree);

private:
    int openDegreeServo;
    int temperature;

    enum
    {
        MANUAL,
        NORMAL

    } state;
    Button *manualButton;
    Motor *window;
    LCD *userConsole;
    MotorImpl* Servo;
};

#endif
