#include "servoTask.h"

#define CHANGE_MODE_TIMEOUT 250

ServoTask::ServoTask(WMMSystem *Machine) : machine(Machine)
{
    Serial.println("ST");
    this->justEntered = true;
    this->currentDegree = 0;
    setState(NORMAL);
    machine->setNormal();
    machine->closeServo();
}

void ServoTask::tick()
{
    checkMsg();
    switch (currentState)
    {
    case NORMAL:
        logOnce("NORMAL");
        machine->openServo(this->currentDegree);
        machine->setDegreeServo(this->currentDegree);
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
            // Attiva il buzzer brevemente per segnalare il messaggio ricevuto


            Serial.println("Message received");

            String content = msg->getContent();
            content.trim();          // Remove spaces and newlines
            content.replace("\"", "");  // Remove extra quotes
        
            Serial.println("Cleaned message: " + content);
        
            // Split the string using ',' as a separator
            int firstComma = content.indexOf(',');
            int secondComma = content.indexOf(',', firstComma + 1);
        
            if (firstComma != -1 && secondComma != -1) {
                String stateStr = content.substring(0, firstComma);
                int apertureInt = content.substring(firstComma + 1, secondComma).toInt();
                float temperature = content.substring(secondComma + 1).toFloat();
        
                if (stateStr == "N") {
                    tone(BUZZER_PIN, 40*10);
                    delay(200);
                    noTone(BUZZER_PIN);
                    Serial.println("Parsed message: Angle=" + String(apertureInt) + " Temperature=" + String(temperature));
    
                    if (apertureInt >= 0 && apertureInt <= 90)
                    {
    
                        tone(BUZZER_PIN, 90*10);
                        delay(200);
                        noTone(BUZZER_PIN);
                        delay(50);
                        Serial.println("Valid angle received, updating system...");
                        this->currentDegree = apertureInt;
                    }
    
                    this->machine->setTemperature((int)temperature); // Converti double in int
    
                    if (currentState == PROBLEM)
                    {
                        setState(NORMAL);
                        machine->setNormal();
                    }
                } else {
                    Serial.println("Invalid state character");
                }
            } else {
                Serial.println("Invalid message format");
            }

            delete msg;
        }
    }
}
