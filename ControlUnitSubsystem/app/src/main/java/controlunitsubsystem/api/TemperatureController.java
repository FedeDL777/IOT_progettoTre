package controlunitsubsystem.api;

public interface TemperatureController {

    void setTemperature(double temperature);

    void setPeriod(Period period);

    double getAverageTemperature();

    double getMaximumTemperature();

    double getMinimumTemperature();

    double getTemperature();

    void freeData();

}
