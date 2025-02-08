package controlunitsubsystem.api;

public interface ControlUnit {

    enum Status {
        NORMAL,
        HOT,
        TOO_HOT,
        ALARM,
        MANUAL,
    }

    double getTemperature();

    void sendTemperatureData();

    void sendStatus();

    void setMotorAngle();

    void sendMotorAngle();

    void solveAlarm();
    
    void tick();
}
