#include "../include/MotorImpl.h"

MotorImpl::MotorImpl(int pin)
{
    this->motor.attach(pin);
    this->motor.write(0);
}

void MotorImpl::fullyOpen()
{
    motor.write(90);
}

void MotorImpl::close()
{
    motor.write(0);
}
void MotorImpl::openDegree(int degree)
{
    degree = (degree < 0) ? 0 : (degree > 90 ? 90 : degree);
    motor.write(degree);
}