package controlunitsubsystem.impl;

import controlunitsubsystem.api.CommChannel;
import controlunitsubsystem.api.MotorController;

public class MotorControllerImpl implements MotorController {

    private int angle;
    private CommChannel motorChannel;

    public MotorControllerImpl(CommChannel motorChannel) {
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
