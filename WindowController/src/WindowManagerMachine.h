#ifndef __WMM__
#define __WMM__

#include <Arduino.h>
#include "include/Button.h"
#include "include/MotorImpl.h"
#include "include/LCD.h"
#include "include/Pot.h"
#define OPEN 90
#define CLOSE 0

class WMMSystem
{
public:
    WMMSystem();
    void init();

    void setManual();
    void setTemperature(int temperature);
    void setDegreeServo(int degree);
    void setNormal();
    void switchState();
    bool isManual();
    bool isNormal();

    void fullyOpenServo();
    void closeServo();
    void openServo(int degree);
    void openManualServo();
    int getServoDegree();

    void showAutomatic();
    void showManual();
    void showProblem();

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
    MotorImpl *Servo;
    Pot *manualSignal;
};

#endif
