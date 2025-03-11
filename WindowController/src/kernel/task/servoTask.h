#include "../Task.h"
#include "../../WindowManagerMachine.h"
#include "../Logger.h"
#include "../MsgService.h"
#ifndef __SERVOTASK__
#define __SERVOTASK__

#include <Arduino.h>

class ServoTask : public Task
{
public:
    ServoTask(WMMSystem *machine);
    void tick();

private:
    WMMSystem *machine;
    void setState(int newState);
    long elapsedTimeInState();
    void logOnce(const String &msg);
    void checkMsg();
    enum
    {
        NORMAL,
        PROBLEM,
        MANUAL,
        MANUAL_DASHBOARD
    };
    int currentState;

    bool justEntered;
    int currentDegree;
    long stateTimestamp;
};

#endif
