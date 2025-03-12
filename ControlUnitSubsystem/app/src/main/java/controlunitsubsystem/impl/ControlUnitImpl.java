package controlunitsubsystem.impl;

import java.util.Scanner;

import controlunitsubsystem.api.ControlUnit;
import controlunitsubsystem.api.HttpPostRequest;

public class ControlUnitImpl implements ControlUnit {

    Scanner lastResponse;
    float lastTemperature;
    Status status;
    int motorAngle;
    final HttpPostRequest dashboard;

    public ControlUnitImpl(String url) {
        this.dashboard = new HttpPostRequestImpl(url);
    }

    @Override
    public Scanner dashboardMessage() {
        return this.dashboard.postReceive(lastTemperature, status.name(), motorAngle);
    }

    @Override
    public void updateTemperature() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateTemperature'");
    }

    @Override
    public void sendMsgToMotor() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'sendMsgToMotor'");
    }

    @Override
    public String receiveMsgFromMotor() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'receiveMsgFromMotor'");
    }

    @Override
    public void tick() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'tick'");
    }
    
}
