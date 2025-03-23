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
    float lastTemperature = 0;
    Status status = Status.NORMAL;
    Status dashboardStatus = Status.NORMAL;
    int motorAngle = 0;
    final HttpPostRequest dashboard;
    CommChannel serialLine;
    MQTTTemperatureReceiver temperatureReceiver;
    MQTTPeriodSender periodSender;

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
        lastTemperature = temperatureReceiver.receiveTemperature(timeout);
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
        serialLine.sendMsg("N;" + motorAngle + ";" + lastTemperature);
    }

    @Override
    public void dashboardTick() {
        try {
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
        updateTemperature(timeout);
        if (status != Status.ALARM || status != Status.DASHBOARD) {
            if (lastTemperature < T1) {
                status = Status.NORMAL;
                motorAngle = 0;
            } else if (lastTemperature < T2) {
                status = Status.HOT;
                motorAngle = Math.round(((lastTemperature - T1) / (T2 - T1)) * 90);
            } else {
                status = Status.TOO_HOT;
                motorAngle = 90;
            }
        }
        return status;
    }
}
