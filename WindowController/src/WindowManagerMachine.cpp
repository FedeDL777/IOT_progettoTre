#include "WindowManagerMachine.h"
#include "include/set_up.h"

WMMSystem::WMMSystem()
{
    this->userConsole = new LCD();
    this->window = new ServoMotor(SERVO_MOTOR);
    this->window->on();
    this->manualSignal = new Pot(POTENTIOMETER);
    this->manualButton = new Button(BUTTON_SWITCH_MANUAL);

    this->userConsole->turnOff();
    this->userConsole->setup();
    this->userConsole->turnOn();

    state = NORMAL; // Imposta lo stato iniziale corretto

    temperature = 20;
    openDegreeServo = 0;
}   

void WMMSystem::switchState()
{   
    state = (state == MANUAL) ? NORMAL : MANUAL;
}

void WMMSystem::setManual()
{
    this->state = MANUAL;
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

void WMMSystem::setTemperature(int temperature)
{
    this->temperature = temperature;
}

void WMMSystem::setDegreeServo(int degree)
{
    this->openDegreeServo = degree;
}

void WMMSystem::fullyOpenServo()
{
    this->window->fullyOpen();
}

void WMMSystem::closeServo()
{
    this->window->close();
}

void WMMSystem::openServo(int degree)
{
    this->window->openDegree(degree);
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

bool WMMSystem::buttonPressed()
{
    this->manualButton->sync();
    return this->manualButton->isPressed();
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
