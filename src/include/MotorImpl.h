#ifndef __MOTORIMPL__
#define __MOTORIMPL__

#include <Servo.h>

#include "Motor.h"

class MotorImpl : public Motor {
   private:
    Servo motor;

   public:
    MotorImpl(int pin);
    void fullyOpen();
    void close();
    void openDegree(int dregree);
};

#endif