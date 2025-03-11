#include "Timer.h"

#include <Arduino.h>

/* period in ms */
void Timer::setupPeriod(int period) {
    this->period = period;
    this->t0 = millis();
}

void Timer::waitForNextTick() {
    /* wait for timer signal */
    delay(deltaOrZero() * 0.95);
    while (millis() - this->t0 < this->period);
    resetTimer();
}

bool Timer::isPeriodPassed() { return millis() - this->t0 > this->period; }

unsigned long Timer::deltaOrZero() {
    long delta = this->period - (millis() - this->t0);
    return delta > 0 ? delta : 0;
}

void Timer::resetTimer() { t0 = millis(); }
