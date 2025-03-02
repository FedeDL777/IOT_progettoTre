#include "WindowManagerMachine.h"
#include "include/set_up.h"

WMMSystem *instance; // Variabile globale per l'istanza

WMMSystem::WMMSystem() : state(NORMAL)
{
    instance = this; // Salviamo il riferimento all'istanza
}

void WMMSystem::switchState()
{
    if (state == MANUAL)
        state = NORMAL;
    else
        state = MANUAL;
}

// Wrapper per chiamare switchState() dall'interrupt
void switchStateWrapper()
{
    if (instance)
    {
        instance->switchState();
    }
}

void WMMSystem::init()
{
    this->userConsole = new LCD();
    this->window = new MotorImpl(SERVO_MOTOR);
    this->manualSignal = new Pot(POTENTIOMETER);
    attachInterrupt(digitalPinToInterrupt(BUTTON_SWITCH_MANUAL), switchStateWrapper, RISING);

    this->userConsole->turnOff();
    this->userConsole->setup();
    this->userConsole->turnOn();
    state = MANUAL; // cambia

    this->temperature = 20;
    this->openDegreeServo = 0;
    //this->userConsole->displayAutomatic(temperature, openDegreeServo);
}

void WMMSystem::setManual()
{
    this->state = MANUAL;
}

void WMMSystem::setTemperature(int temperature)
{
    this->temperature = temperature;
}

void WMMSystem::setDegreeServo(int degree)
{
    this->openDegreeServo = degree;
}

void WMMSystem::setNormal()
{
    this->state = NORMAL;
}

bool WMMSystem::isManual()
{
    return state == MANUAL;
}

bool WMMSystem::isNormal()
{
    return state == NORMAL;
}

void WMMSystem::fullyOpenServo()
{
    this->Servo->fullyOpen();
}

void WMMSystem::closeServo()
{
    this->Servo->close();
}

void WMMSystem::openServo(int degree)
{
    this->Servo->openDegree(degree);
}

void WMMSystem::openManualServo()
{
    if (state != MANUAL)
        return;
    openDegreeServo = manualSignal->getDegree();
    this->openServo(openDegreeServo);
}

int WMMSystem::getServoDegree()
{
    return this->openDegreeServo;
}

void WMMSystem::showAutomatic()
{
    this->userConsole->displayAutomatic(temperature, openDegreeServo);
}

void WMMSystem::showManual()
{
    this->userConsole->displayManual(temperature, openDegreeServo);
}

void WMMSystem::showProblem()
{
    this->userConsole->displayProblem();
}
