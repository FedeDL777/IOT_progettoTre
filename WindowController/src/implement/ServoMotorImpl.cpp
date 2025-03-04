#include "../include/ServoMotor.h"
#include "Arduino.h"
#include "../include/ServoTimer2.h"
#define MIN_DEGREE 0
#define MAX_DEGREE 90
ServoMotor::ServoMotor(int pin){
  this->pin = pin;  
} 

void ServoMotor::on(){
  // updated values: min is 544, max 2400 (see ServoTimer2 doc)
  motor.attach(pin); //, 544, 2400);    
}

void ServoMotor::openDegree(int angle){
	if (angle > MAX_DEGREE){
		angle = MAX_DEGREE;
	} else if (angle < MIN_DEGREE){
		angle = MIN_DEGREE;
	}
  // 750 -> 0, 2250 -> 180 
  // 750 + angle*(2250-750)/180
  // updated values: min is 544, max 2400 (see ServoTimer2 doc)
  float coeff = (2400.0-544.0)/180;
  motor.write(544 + angle*coeff);              
}

void ServoMotor::off(){
  motor.detach();    
}

void ServoMotor::fullyOpen()
{
  this->openDegree(180);
}

void ServoMotor::close()
{
  this->openDegree(0);
}
