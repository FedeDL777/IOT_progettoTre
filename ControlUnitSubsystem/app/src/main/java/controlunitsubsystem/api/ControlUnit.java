package controlunitsubsystem.api;

public interface ControlUnit {

    enum Status {
        NORMAL,
        HOT,
        TOO_HOT,
        ALARM,
        MANUAL,
    }

    void setTemperature(double temperature);

    void sendStatus();

    void setMotorAngle();

    void sendMotorAngle();

    void solveAlarm();
    
    void tick();
}
