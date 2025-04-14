package controlunitsubsystem.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.paho.client.mqttv3.MqttException;

import controlunitsubsystem.api.CommChannel;
import controlunitsubsystem.api.ControlUnit;
import controlunitsubsystem.api.HttpPostRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

public class ControlUnitImpl implements ControlUnit {

    Map<String, String> lastResponse = new HashMap<>();
    float lastTemperature = 15;
    Status status = Status.NORMAL;
    Status dashboardStatus = Status.NORMAL;
    int motorAngle = 0;
    final HttpPostRequest dashboard;
    CommChannel serialLine;
    MQTTTemperatureReceiver temperatureReceiver;
    MQTTPeriodSender periodSender;
    boolean increasing = true; // Flag to indicate if the temperature is increasing or decreasing in debug mode
    long tooHotStartTime = 0; // Timestamp when the system enters TOO_HOT state

    final float T1 = 23;
    final float T2 = 26;

    public ControlUnitImpl(String url, String comPortName) {
        try {
            this.serialLine = new SerialCommChannel(comPortName, 9600);
        } catch (Exception e) {
            System.err.println("Error while initializing serial line: " + e.getMessage());
            System.exit(1);
        }

        try {
            this.temperatureReceiver = new MQTTTemperatureReceiver();
        } catch (MqttException e) {
            System.err.println("Error while initializing MQTT receiver: " + e.getMessage());
            System.exit(1);
        }

        try {
            this.periodSender = new MQTTPeriodSender();
        } catch (MqttException e) {
            System.err.println("Error while initializing MQTT sender: " + e.getMessage());
            System.exit(1);
        }

        this.dashboard = new HttpPostRequestImpl(url);
    }

    @Override
    public String dashboardMessage() throws IOException {
        return this.dashboard.postReceive(lastTemperature, status.name(), motorAngle);
    }

    
    @Override
    public void updateTemperature(int timeout) {
        Float newTemperature = temperatureReceiver.receiveTemperature(timeout);
        while(newTemperature == null){
            newTemperature = temperatureReceiver.receiveTemperature(timeout);
        }
        lastTemperature = newTemperature;
        
    }

    
    private float generateTemperature() {
    
        if (increasing) {
            lastTemperature += 0.5f; // Aumento graduale
            if (lastTemperature >= 30.0f) {
                increasing = false;  // Quando arriva a 30°C, cambia stato
            }
        } else {
            lastTemperature = 20.0f; // Reset improvviso a 20°C
            increasing = true; // Ricomincia a salire
        }
    
        return lastTemperature;
    }
    
    @Override
    public void sendPeriod(int period) {
        try {
            periodSender.sendPeriod(period);
        } catch (MqttException e) {
            System.err.println("Error while sending period: " + e.getMessage());
        }
    }

    @Override
    public void sendMsgToMotor() {
        serialLine.sendMsg("N," + motorAngle + "," + lastTemperature);
    }

    @Override
    public void dashboardTick() {
        try {
            System.err.println("dashboardTick: " + status.name() + " motorAngle: " + motorAngle);
            String response = dashboardMessage();
            ObjectMapper mapper = new ObjectMapper();
            try {
                lastResponse = mapper.readValue(response, new TypeReference<Map<String, String>>() {
                });
                if (lastResponse.get("status").equals("UNDEFINED")) {
                    status = dashboardStatus;
                } else if (lastResponse.get("status").equals("MANUAL")) {
                    // status = Status.DASHBOARD;
                    dashboardStatus = Status.DASHBOARD;
                } else if (lastResponse.get("status").equals("NORMAL")) {
                    dashboardStatus = Status.NORMAL;
                }
                motorAngle = Integer.parseInt(lastResponse.get("window_level"));// always the angle to send to the motor
                System.out.println("sending motorAngle: " + motorAngle);
                sendMsgToMotor();

                System.out.println("status: " + dashboardStatus.name() + " motorAngle: " + motorAngle);
            } catch (Exception e) {
                System.err.println("Error while parsing response from dashboard: " + e.getMessage());
            }
        } catch (IOException e) {
            System.err.println("Error while sending message to dashboard: " + e.getMessage());
        }

    }

    @Override
    public void destroy() {
        serialLine.close();
        temperatureReceiver.disconnect();
        periodSender.disconnect();
    }

    @Override
    public Status updateMotorAndStatusTick(int timeout) {
        System.err.println("updateMotorAndStatusTick: " + status.name() + " motorAngle: " + motorAngle);
    updateTemperature(timeout);

    if (status != Status.ALARM && status != Status.DASHBOARD) {
        if (lastTemperature < T1) {
            status = Status.NORMAL;
            motorAngle = 0;
            tooHotStartTime = 0; // Resetta il timer
        } else if (lastTemperature < T2) {
            status = Status.HOT;
            motorAngle = Math.round(((lastTemperature - T1) / (T2 - T1)) * 90);
            tooHotStartTime = 0; // Resetta il timer
        } else {
            if (status != Status.TOO_HOT) {
                tooHotStartTime = System.currentTimeMillis(); // Salva il tempo di ingresso
            }
            status = Status.TOO_HOT;
            motorAngle = 90;

            // Se è passato più di 1 secondo in TOO_HOT, cambia in ALARM
            if (System.currentTimeMillis() - tooHotStartTime >= 1000) {
                status = Status.ALARM;
            }
        }
    }

    return status;
    }
}
