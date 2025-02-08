package controlunitsubsystem.impl;

import java.util.concurrent.atomic.AtomicInteger;

public class ControllerStatesTask extends Thread{
    
    private ControlUnitImpl controlUnit;
    private AtomicInteger period;
    private boolean running = true;

    public ControllerStatesTask(ControlUnitImpl controlUnit, int period) {
        this.controlUnit = controlUnit;
        this.period = new AtomicInteger(period);
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(period.get());
                controlUnit.tick();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void changePeriod(int period) {
        this.period.set(period);
    }

    public void stopTask() {
        running = false;
    }
    
}
