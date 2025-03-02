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
  machine = new WMMSystem();
  machine->init();
  sched = new Scheduler();
  Task *servoTask = new ServoTask(machine);
  servoTask->init(100);
  sched->addTask(servoTask);
}

void loop()
{
  machine->fullyOpenServo();
  Serial.println(machine->isManual());
  sched->schedule();
}
