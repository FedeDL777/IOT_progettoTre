#ifndef __WMM__
#define __WMM__

#include <Arduino.h>
#include "include/Button.h"
#include "include/Led.h"
#include "include/MotorImpl.h"
#include "include/LCD.h"
//#include "include/ServoMotor.h"


class WMMSystem {
public:
    WMMSystem();
    void init();
    
    void manual();
    void normal();
    bool isManual();
    bool isNormal();



    void fullyOpenServo();
    void closeServo();
    void openServo(int degree);

    
    private:
        int openDegreeServo;
        

        enum{
            MANUAL, NORMAL, ERROR

        } state;
    Button* manualButton;
    Motor* window;
    LCD* userConsole;
};


#endif
