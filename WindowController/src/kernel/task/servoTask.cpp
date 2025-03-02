#include "servoTask.h"

ServoTask::ServoTask(WMMSystem *Machine) : machine(Machine)
{

    this->justEntered = true;
    this->currentDegree = 0;
    setState(MANUAL);
    machine->setNormal();
    machine->closeServo();
}
/*
void ServoTask::tick()
{
    checkMsg();
    switch (currentState)
    {
    case NORMAL:
        logOnce("NORMAL");
        machine->openServo(currentDegree);
        machine->setDegreeServo(currentDegree);
        machine->showAutomatic();
        break;
    case PROBLEM:
        logOnce("PROBLEM");
        machine->showProblem();
        break;
    case MANUAL:
        logOnce("MANUAL");
        machine->openManualServo();
        machine->showManual();
        currentDegree = machine->getServoDegree();
        break;
    }
}*/
void ServoTask::tick()
{
    checkMsg();

    logOnce("MANUAL");
    machine->openManualServo();
    machine->showManual();
    currentDegree = machine->getServoDegree();
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
        justEntered = false;
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

            if (sscanf(contentCopy, "%c;%d;%d", &stateChar, &apertureInt, &temperature) == 2)
            {
                if (apertureInt >= 0 && apertureInt <= 90)
                { // forse da cambiare la logica
                    this->currentDegree = apertureInt;
                }
                this->machine->setTemperature((int)temperature);
                if (stateChar == 'P')
                {
                    setState(PROBLEM);
                }
                else if (currentState == PROBLEM && stateChar != 'P')
                {
                    setState(NORMAL);
                }
            }
        }
    }
}
