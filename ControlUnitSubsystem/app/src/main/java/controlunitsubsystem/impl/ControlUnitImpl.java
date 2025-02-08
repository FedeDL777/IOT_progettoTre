package controlunitsubsystem.impl;

import controlunitsubsystem.api.CommChannel;
import controlunitsubsystem.api.ControlUnit;
import controlunitsubsystem.api.Motor;
import controlunitsubsystem.api.TemperatureController;

public class ControlUnitImpl implements ControlUnit {
    private final Motor motorController;
    private final TemperatureController temperatureController;
    private Status status;
    private final CommChannel serialChannel;
    private final ControllerStatesTask controllerStatesTask;


    private final int P1 = 200;
    private final int P2 = 100;
    private final double T1 = 21;
    private final double T2 = 30;
    private final long DT = 2000;
    private final int MAX_ANGLE = 90;
    private final int MIN_ANGLE = 0;
    private long lastCheck = 0;

    public ControlUnitImpl(String port, int rate) throws Exception {
        this.serialChannel = new SerialCommChannel(port, rate);
        this.motorController = new MotorImpl(serialChannel);
        this.temperatureController = new TemperatureControllerImpl();
        controllerStatesTask = new ControllerStatesTask(this, P1);
        this.status = Status.NORMAL;
    }

    @Override
    public double getTemperature() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTemperature'");
    }

    @Override
    public void sendTemperatureData() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'sendTemperatureData'");
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
            motorController.sendMotorAngle(MIN_ANGLE);
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
