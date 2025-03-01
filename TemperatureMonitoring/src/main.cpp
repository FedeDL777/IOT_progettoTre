/*
 * HTTPClient lib --  Performing an HTTP POST to our REST service
 *
 * Remark:
 * - Going through ngrok
 *
 */
#include <WiFi.h>
#include <HTTPClient.h>
#include "kernel/Scheduler.h"

Scheduler sched;


void setup(){

  sched.init(100);
  


}

void loop(){
  sched.schedule();
}
