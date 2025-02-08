package controlunitsubsystem.api;

import java.util.List;

public interface TemperatureController {

    void setTemperature(double temperature);

    void setPeriod(Period period);

    double getAverageTemperature();

    double getMaximumTemperature();

    double getMinimumTemperature();

    double getTemperature();

    void freeData();

    List<Double> getLastNTemperatures(int n);
}
