package controlunitsubsystem.impl;

import controlunitsubsystem.api.CommChannel;
import controlunitsubsystem.api.ControlUnit;
import controlunitsubsystem.api.Motor;

public class ControlUnitImpl2 implements ControlUnit {
    private final Motor motorController;
    private final TemperatureController temperatureController;
    private Status status;
    private final CommChannel serialChannel;
    private final CommChannel espChannel;
    private final ControllerStatesTask controllerStatesTask;
    private final TemperatureTask temperatureTask;


    private final int P1 = 200;
    private final int P2 = 100;
    private final double T1 = 21;
    private final double T2 = 30;
    private final long DT = 2000;
    private final int MAX_ANGLE = 90;
    private final int MIN_ANGLE = 0;
    private long lastCheck = 0;

    public ControlUnitImpl2(String port, int rate) throws Exception {
        this.serialChannel = new SerialCommChannel(port, rate);
        this.motorController = new MotorImpl(serialChannel);
        this.temperatureController = new TemperatureControllerImpl();
        controllerStatesTask = new ControllerStatesTask(this, P1);
        this.status = Status.NORMAL;
        controllerStatesTask.start();
    }

    @Override
    public void setTemperature(double temperature) {
        temperatureController.setTemperature(temperature);
    }

    @Override
    public void sendStatus() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'sendStatus'");
    }

    @Override
    public void setMotorAngle() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setMotorAngle'");
    }

    @Override
    public void sendMotorAngle() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'sendMotorAngle'");
    }

    @Override
    public void solveAlarm() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'solveAlarm'");
    }

    @Override
    public void tick() {
        double temperature = temperatureController.getTemperature();
        updateStatus(temperature);
    }

    private void updateStatus(double temperature) {
        if(temperature<=T1) {
            status = Status.NORMAL;
            controllerStatesTask.changePeriod(P1);
            motorController.sendMotorAngle(MIN_ANGLE);//this is blocking must create an executor
        } else if(temperature<=T2) {
            status = Status.HOT;
            controllerStatesTask.changePeriod(P2);
            motorController.sendMotorAngle(fromTemperatureToAngle(temperature));
        } else {
            if(status != Status.TOO_HOT) {
                controllerStatesTask.changePeriod(P2);
                lastCheck = System.currentTimeMillis();
                status = Status.TOO_HOT;
            } else if(System.currentTimeMillis() - lastCheck >= DT) {
                status = Status.ALARM;
            }
            motorController.sendMotorAngle(MAX_ANGLE);
        }
    }

    private int fromTemperatureToAngle(double temperature) {
        return MIN_ANGLE + (MAX_ANGLE - MIN_ANGLE) * (int) Math.round((temperature - T1) / (T2 - T1));
    }
}
