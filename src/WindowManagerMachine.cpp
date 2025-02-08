#include "WindowManagerMachine.h"
#include "include/set_up.h"

WMMSystem::WMMSystem(){
}

void WMMSystem::init()
{
    //inizializzazione LCD
    this->userConsole = new LCD();

    // Inizializzazione del servo motore
    this->window = new MotorImpl(SERVO_MOTOR);

    //Inizializzazione bottone
    //TODO

    this->userConsole->turnOff();
    this->userConsole->setup();
    this->userConsole->turnOn();
    state = NORMAL;

}

void WMMSystem::manual()
{
}

void WMMSystem::normal()
{
}

bool WMMSystem::isManual()
{
    return false;
}

bool WMMSystem::isNormal()
{
    return false;
}

void WMMSystem::fullyOpenServo()
{
}

void WMMSystem::closeServo()
{
}

void WMMSystem::openServo(int degree)
{
}
