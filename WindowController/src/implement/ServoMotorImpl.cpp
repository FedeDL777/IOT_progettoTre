#include "../include/ServoMotor.h"
#include "Arduino.h"

#define MIN_DEGREE 0
#define MAX_DEGREE 90
ServoMotor::ServoMotor(const int pin) : pin(pin) { }

void ServoMotor::on()
{
  // updated values: min is 544, max 2400 (see ServoTimer2 doc)
  motor.attach(pin); //, 544, 2400);
}

void ServoMotor::openDegree(int angle)
{

  int correctedAngle = angle;
  if (angle > MAX_DEGREE)
  {
    correctedAngle = MAX_DEGREE;
  }
  else if (angle < MIN_DEGREE)
  {
    correctedAngle = MIN_DEGREE;
  }

  this->motor.write(correctedAngle);

}

void ServoMotor::off()
{
  motor.detach();
}

void ServoMotor::fullyOpen()
{
  this->openDegree(MAX_DEGREE);
}

void ServoMotor::close()
{
  this->openDegree(MIN_DEGREE);
}
