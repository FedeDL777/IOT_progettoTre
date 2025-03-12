package controlunitsubsystem.api;

import java.util.Scanner;

public interface ControlUnit {

    enum Status {
        NORMAL,
        HOT,
        TOO_HOT,
        ALARM,
        MANUAL,
        DASHBOARD,
    }

    /**
     * Sends the message to the dashboard.
     * 
     * @return the response of the dashboard.
     */
    Scanner dashboardMessage();

    void updateTemperature();//TODO: don't know MQTT

    void sendMsgToMotor();

    String receiveMsgFromMotor();
    
    void tick();
}
