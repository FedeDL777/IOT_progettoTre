#include "WindowManagerMachine.h"
#include "include/set_up.h"

WMMSystem* instance;  // Variabile globale per l'istanza

WMMSystem::WMMSystem() : state(NORMAL) {
    instance = this;  // Salviamo il riferimento all'istanza
}

void WMMSystem::switchState() {
    if (state == MANUAL)
        state = NORMAL;
    else
        state = MANUAL;
}

// Wrapper per chiamare switchState() dall'interrupt
void switchStateWrapper() {
    if (instance) {
        instance->switchState();
    }
}

void WMMSystem::init() {
    this->userConsole = new LCD();
    this->window = new MotorImpl(SERVO_MOTOR);

    attachInterrupt(digitalPinToInterrupt(BUTTON_SWITCH_MANUAL), switchStateWrapper, RISING);

    this->userConsole->turnOff();
    this->userConsole->setup();
    this->userConsole->turnOn();
    state = NORMAL;
    this->temperature = 20;
    this->openDegreeServo = 0;
}

bool WMMSystem::isManual() {
    return state == MANUAL;
}

bool WMMSystem::isNormal() {
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
