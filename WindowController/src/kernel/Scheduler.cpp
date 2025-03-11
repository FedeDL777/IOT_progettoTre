#include "Scheduler.h"

volatile bool timerFlag;
Timer timer;

void timerHandler(void) { timerFlag = true; }

void Scheduler::init(int basePeriod) {
    timer.setupPeriod(basePeriod);
    this->basePeriod = basePeriod;
    nTasks = 0;
}

bool Scheduler::addTask(Task* task) {
    if (nTasks < MAX_TASKS - 1) {
        taskList[nTasks] = task;
        nTasks++;
        return true;
    } else {
        return false;
    }
}

void Scheduler::schedule() {
    timer.waitForNextTick();
    // Serial.println("here");
    for (int i = 0; i < nTasks; i++) {
        if (taskList[i]->updateAndCheckTime(basePeriod)) {
            taskList[i]->tick();
        }
    }
}
