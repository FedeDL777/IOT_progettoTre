#include <Wire.h>
#include <LiquidCrystal_I2C.h>
#include "../include/set_up.h"
#include "../include/LCD.h"

LCD::LCD()
{
    pLcd = new LiquidCrystal_I2C(0x27, 20, 4);
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
void LCD::displayManual(int t, int d)
{
    this->turnOn();
    this->initCursor();
    pLcd->print("MANUAL degree: ");
    pLcd->print(mapOpening(d));
    pLcd->print("%");
    pLcd->setCursor(1, 2);
    pLcd->print("Temperature: ");
    pLcd->print(t);
}
void LCD::displayAutomatic(int t, int)
{
    this->turnOn();
    this->initCursor();
    pLcd->print("Automatic");
    pLcd->setCursor(1, 2);
    pLcd->print("Temperature: ");
    pLcd->print(t);
}

void LCD::displayProblem()
{
    this->turnOn();
    this->initCursor();
    pLcd->print("Problem");
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
int LCD::mapOpening(int d)
{
    d *= 1.11;
    d += 0.1;
    return d;
}
