package controlunitsubsystem.api;

public interface Comunicator {
    
    void sendData(String data);

    String receiveData();

}
