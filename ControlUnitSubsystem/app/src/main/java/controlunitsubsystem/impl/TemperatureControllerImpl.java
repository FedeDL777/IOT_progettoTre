package controlunitsubsystem.impl;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import com.google.common.util.concurrent.AtomicDouble;

import controlunitsubsystem.api.Period;
import controlunitsubsystem.api.TemperatureController;

public class TemperatureControllerImpl implements TemperatureController {

    private AtomicDouble lastTemperature;
    private AtomicDouble maxTemperature;
    private AtomicDouble minTemperature;
    private List<TempEntry> temperatures;
    private Period period;

    public TemperatureControllerImpl() {
        maxTemperature = new AtomicDouble(Double.MIN_VALUE);
        minTemperature = new AtomicDouble(Double.MAX_VALUE);
        temperatures = new LinkedList<>();
        period = Period.DAY;
    }

    @Override
    public void setTemperature(double temperature) {
        temperatures.add(new TempEntry(temperature, LocalDateTime.now()));
        lastTemperature.set(temperature);
        if (temperature >= maxTemperature.get()) {
            maxTemperature.set(temperature);
        }
        if (temperature <= minTemperature.get()) {
            minTemperature.set(temperature);
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
        return maxTemperature.get();
    }

    @Override
    public double getMinimumTemperature() {
        return minTemperature.get();
    }

    @Override
    public double getTemperature() {
        return lastTemperature.get();
    }

    @Override
    public void freeData() {
        while (temperatures.getFirst().getTimestamp().isBefore(LocalDateTime.now().minusDays(1))) {
            temperatures.removeFirst();
        }
    }

    @Override
    public List<Double> getLastNTemperatures(int n) {
        List<Double> lastNTemps = new LinkedList<>();
        for (int i = temperatures.size() - 1; i >= 0 && n > 0; i--, n--) {
            lastNTemps.addFirst(temperatures.get(i).getTemperature());
        }
        return lastNTemps;
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
