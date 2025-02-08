#include <Wire.h> 
#include <LiquidCrystal_I2C.h>
#include "../include/set_up.h"
#include  "../include/LCD.h"



LCD::LCD()
{
    pLcd = new LiquidCrystal_I2C(0x27,20,4);
}
void LCD::setup()
{
    pLcd->clear();
    pLcd->init();

  pLcd->backlight();
  pLcd->noDisplay();
}



void LCD::turnOn()
{
    pLcd->backlight();
    pLcd->display();
  pLcd->clear();
}
void LCD::displayManual()
{
    this->turnOn();
    this->initCursor();
    pLcd->print("MANUAL");
}
void LCD::displayNormal()
{
    this->turnOn();
    this->initCursor();
    pLcd->print("NORMAL");
}

void LCD::turnOff()
{
    pLcd->noDisplay();
    pLcd->noBacklight();
}

void LCD::initCursor()
{
    pLcd->clear();
    pLcd->setCursor(1, 0); 
}

