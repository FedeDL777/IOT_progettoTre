#ifndef __SERVO_MOTOR__
#define __SERVO_MOTOR__


#include <arduino.h>
#include "ServoTimer2.h"
#include "../include/ServoTimer2.h"
class ServoMotor{

public:
  ServoMotor(int pin);

  void on();
  void off();
  void fullyOpen();
  void close();
  void openDegree(int angle);  
private:
  int pin; 
  ServoTimer2 motor; 
};

#endif
