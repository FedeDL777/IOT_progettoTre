package controlunitsubsystem.api;

import java.io.IOException;

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
    String dashboardMessage() throws IOException;

    void updateTemperature();//TODO: don't know MQTT, this method should receive the temperature from the sensor with a thread listening on the MQTT line

    void sendMsgToMotor();

    String receiveMsgFromMotor();
    
    void dashboardTick();
}
