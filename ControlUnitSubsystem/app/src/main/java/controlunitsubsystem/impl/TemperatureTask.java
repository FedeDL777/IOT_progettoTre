package controlunitsubsystem.impl;

import controlunitsubsystem.api.CommChannel;

public class TemperatureTask extends Thread{
    private ControlUnitImpl controlUnit;
    private CommChannel tempChannel;
    private boolean running = true;

    public TemperatureTask(ControlUnitImpl controlUnit, CommChannel espChannel){
        this.controlUnit = controlUnit;
        this.tempChannel = espChannel;
    }

    @Override
    public void run() {
        while (running) {
            try {
                String msg = tempChannel.receiveMsg();
                controlUnit.setTemperature(Double.parseDouble(msg));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
