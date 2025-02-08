package controlunitsubsystem.impl;

import controlunitsubsystem.api.CommChannel;
import controlunitsubsystem.api.Motor;

public class MotorImpl implements Motor {

    private int angle;
    private CommChannel motorChannel;

    public MotorImpl(CommChannel motorChannel) {
        this.motorChannel = motorChannel;
    }

    @Override
    public void sendMotorAngle(int angle) {
        this.angle = angle;
        motorChannel.sendMsg(Integer.toString(angle));
    }
    
    @Override
    public int getMotorAngle() {
        return angle;
    }
}
