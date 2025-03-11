#ifndef __TIMER__
#define __TIMER__

class Timer {
   private:
    unsigned long period;
    unsigned long t0;

    unsigned long deltaOrZero();

   public:
    /* period in ms */
    void setupPeriod(int period);
    void waitForNextTick();
    bool isPeriodPassed();
    void resetTimer();
};

#endif
