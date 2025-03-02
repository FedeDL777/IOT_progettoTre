#include <Arduino.h>
#include "WindowManagerMachine.h"
#include "kernel/task/servoTask.h"
#include "kernel/Task.h"
#include "kernel/Scheduler.h"
#include "kernel/Logger.h"
#include "kernel/MsgService.h"

WMMSystem *machine;
Scheduler *sched;
void setup()
{
  MsgService.init();
  sched->init(100);

  machine = new WMMSystem();
  machine->init();

  Task *servoTask = new ServoTask(machine);
  servoTask->init(100);
  sched->addTask(servoTask);
}

void loop()
{
  sched->schedule();
}
