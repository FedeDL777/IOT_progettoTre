package controlunitsubsystem.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

    public ControlUnitImpl(String url) {
        this.dashboard = new HttpPostRequestImpl(url);
    }

    @Override
    public String dashboardMessage() throws IOException{
        return this.dashboard.postReceive(lastTemperature, status.name(), motorAngle);
    }

    @Override
    public void updateTemperature() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateTemperature'");
    }

    @Override
    public void sendMsgToMotor() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'sendMsgToMotor'");
    }

    @Override
    public String receiveMsgFromMotor() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'receiveMsgFromMotor'");
    }

    @Override
    public void dashboardTick() {
        try{
            String response = dashboardMessage();
            ObjectMapper mapper = new ObjectMapper();
            try{
                lastResponse = mapper.readValue(response, new TypeReference<Map<String, String>>() {});
                if(lastResponse.get("status").equals("UNDEFINED")) {
                    status = dashboardStatus;
                } else if(lastResponse.get("status").equals("MANUAL")) {
                    status = Status.DASHBOARD;
                    dashboardStatus = Status.DASHBOARD;
                } else if(lastResponse.get("status").equals("NORMAL")){
                    dashboardStatus = Status.NORMAL;
                }
                motorAngle = Integer.parseInt(lastResponse.get("window_level"));

                System.out.println("status: " + dashboardStatus.name() + " motorAngle: " + motorAngle);
            } catch (Exception e) {
                System.err.println("Error while parsing response from dashboard: " + e.getMessage());
            }
        } catch (IOException e) {
            System.err.println("Error while sending message to dashboard: " + e.getMessage());
        }

    }
    
}
