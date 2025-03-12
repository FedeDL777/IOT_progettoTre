package controlunitsubsystem.impl;

import controlunitsubsystem.api.CommChannel;
/* 
#define NORMAL_SAMPLE 5000
#define HOT_SAMPLE 1000
#define ALLARM_TIME 10000
enum TemperatureState
{
    NORMAL,
    HOT,
    TOO_HOT,
    ALLARM
} temperatureState;*/
public class TemperatureTask extends Thread{
    private ControlUnitImpl2 controlUnit;
    private CommChannel tempChannel;
    private boolean running = true;

    
    public TemperatureTask(ControlUnitImpl2 controlUnit, CommChannel espChannel){
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
