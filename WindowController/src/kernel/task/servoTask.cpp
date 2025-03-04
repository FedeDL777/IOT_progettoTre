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
    justEntered = true;
}

long ServoTask::elapsedTimeInState()
{
    return millis() - stateTimestamp;
}

void ServoTask::logOnce(const String &msg)
{
    if (justEntered)
    {
        Logger.log(msg);
        justEntered = false; // Assicura che venga resettato solo dopo il log
    }
}

void ServoTask::checkMsg()
{
    if (this->currentState != PROBLEM)
    {
        if (machine->isManual())
            setState(MANUAL);
        else
            setState(NORMAL);
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
                }
                this->machine->setTemperature((int)temperature); // Convertiamo double -> int

                if (stateChar == 'P')
                {
                    setState(PROBLEM);
                }
                else if (currentState == PROBLEM && stateChar != 'P')
                {
                    setState(NORMAL);
                }
            }
            delete msg;
        }
    }
}