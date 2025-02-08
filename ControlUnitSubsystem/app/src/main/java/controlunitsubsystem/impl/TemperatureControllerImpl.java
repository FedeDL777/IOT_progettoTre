package controlunitsubsystem.impl;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import controlunitsubsystem.api.Period;
import controlunitsubsystem.api.TemperatureController;

public class TemperatureControllerImpl implements TemperatureController {

    private double lastTemperature;
    private double maxTemperature;
    private double minTemperature;
    private List<TempEntry> temperatures;
    private Period period;

    public TemperatureControllerImpl() {
        maxTemperature = Double.MIN_VALUE;
        minTemperature = Double.MAX_VALUE;
        temperatures = new LinkedList<>();
        period = Period.DAY;
    }

    @Override
    public void setTemperature(double temperature) {
        temperatures.add(new TempEntry(temperature, LocalDateTime.now()));
        lastTemperature = temperature;
        if (temperature > maxTemperature) {
            maxTemperature = temperature;
        }
        if (temperature < minTemperature) {
            minTemperature = temperature;
        }
    }

    @Override
    public void setPeriod(Period period) {
        this.period = period;
    }

    @Override
    public double getAverageTemperature() {
        double sum = 0;
        long size = 0;
        switch (period) {
            case DAY:
                for (TempEntry entry : temperatures) {
                    if (entry.getTimestamp().isAfter(LocalDateTime.now().minusDays(1))) {
                        sum += entry.getTemperature();
                        size++;
                    }
                }

                break;
            case HOUR:
                for (TempEntry entry : temperatures) {
                    if (entry.getTimestamp().isAfter(LocalDateTime.now().minusHours(1))) {
                        sum += entry.getTemperature();
                        size++;
                    }
                }
                break;
            case MINUTE:
                for (TempEntry entry : temperatures) {
                    if (entry.getTimestamp().isAfter(LocalDateTime.now().minusMinutes(1))) {
                        sum += entry.getTemperature();
                        size++;
                    }
                }
                break;
        }
        if (size == 0) {
            return 0;
        } else {
            return sum / size;
        }
    }

    @Override
    public double getMaximumTemperature() {
        return maxTemperature;
    }

    @Override
    public double getMinimumTemperature() {
        return minTemperature;
    }

    @Override
    public double getTemperature() {
        return lastTemperature;
    }

    @Override
    public void freeData() {
        while(temperatures.getFirst().getTimestamp().isBefore(LocalDateTime.now().minusDays(1))) {
            temperatures.removeFirst();
        }
    }

    private class TempEntry {
        private final double temperature;
        private final LocalDateTime insertTime;

        public TempEntry(double temperature, LocalDateTime insertTime) {
            this.temperature = temperature;
            this.insertTime = insertTime;
        }

        public double getTemperature() {
            return temperature;
        }

        public LocalDateTime getTimestamp() {
            return insertTime;
        }

        @Override
        public String toString() {
            return "Temp: " + temperature + "°C, Ora: " + insertTime;
        }
    }
}
