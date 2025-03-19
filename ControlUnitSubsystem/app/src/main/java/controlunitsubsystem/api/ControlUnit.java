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

    void updateTemperature(int timeoutMillis);

    void sendPeriod(int period);

    void sendMsgToMotor();
    
    void dashboardTick();

    /**
     * Updates the motor and the status.
     * @return the status of the system.
     */
    Status updateMotorAndStatusTick(int timeoutMillis);

    void destroy();
}
