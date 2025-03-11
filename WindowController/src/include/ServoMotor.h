#ifndef __SERVO_MOTOR__
#define __SERVO_MOTOR__


#include <arduino.h>

#include <Servo.h>	


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
  Servo motor; 
};

#endif
