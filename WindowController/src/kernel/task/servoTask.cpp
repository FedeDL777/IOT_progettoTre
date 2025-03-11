#include "servoTask.h"

#define CHANGE_MODE_TIMEOUT 250

ServoTask::ServoTask(WMMSystem *Machine) : machine(Machine)
{
    Serial.println("ST");
    this->justEntered = true;
    this->currentDegree = 0;
    setState(MANUAL);
    machine->setManual();
    machine->closeServo();
}

void ServoTask::tick()
{
    checkMsg();
    switch (currentState)
    {
    case NORMAL:
        logOnce("NORMAL");
        machine->openServo(currentDegree);
        machine->setDegreeServo(currentDegree);
        if (this->machine->buttonPressed() && this->elapsedTimeInState() > CHANGE_MODE_TIMEOUT)
        {
            setState(MANUAL);
            machine->setManual();
        }
        machine->showAutomatic();
        break;

    case PROBLEM:
        logOnce("PROBLEM");
        machine->showProblem();
        break;

    case MANUAL:
        logOnce("MANUAL");
        machine->openManualServo();
        if (this->machine->buttonPressed() && this->elapsedTimeInState() > CHANGE_MODE_TIMEOUT)
        {
            setState(NORMAL);
            machine->setNormal();
        }
        machine->showManual();
        currentDegree = machine->getServoDegree();
        break;
    }
}

void ServoTask::setState(int newState)
{
    currentState = newState;
    stateTimestamp = millis();
    this->justEntered = true;
}

long ServoTask::elapsedTimeInState()
{
    return millis() - stateTimestamp;
}

void ServoTask::logOnce(const String &msg)
{
    if (this->justEntered)
    {
        Logger.log(msg);
        this->justEntered = false; // Assicura che venga resettato solo dopo il log
    }
}

void ServoTask::checkMsg()
{
    if (this->currentState != PROBLEM)
    {
        if (machine->isManual() && this->currentState != MANUAL)
        {
            setState(MANUAL);
            machine->setManual();
        }

        else if (this->currentState != NORMAL && this->currentState != MANUAL)
        {
            setState(NORMAL);
            machine->setNormal();
        }
    }

    if (MsgService.isMsgAvailable())
    {
        logOnce(F("[ST] Message available"));
        Msg *msg = MsgService.receiveMsg();
        if (msg != NULL)
        {
            String content = msg->getContent();
            char contentCopy[content.length() + 1];
            strcpy(contentCopy, content.c_str());

            int apertureInt;
            double temperature;
            char stateChar;

            // Corretto sscanf con "%lf" per leggere correttamente il double
            if (sscanf(contentCopy, "%c;%d;%lf", &stateChar, &apertureInt, &temperature) == 3)
            {
                if (apertureInt >= 0 && apertureInt <= 90)
                {
                    this->currentDegree = apertureInt;
                    setState(NORMAL);
                    machine->setNormal();
                }
                this->machine->setTemperature((int)temperature); // Convertiamo double -> int

                if (stateChar == 'P')
                {
                    setState(PROBLEM);
                }
                else if (currentState == PROBLEM && stateChar != 'P')
                {
                    setState(NORMAL);
                    machine->setNormal();
                }
            }
            delete msg;
        }
    }
}