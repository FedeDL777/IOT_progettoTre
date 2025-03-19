package controlunitsubsystem.impl;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class MQTTTemperatureReceiver {

    private MqttClient client;
    private final String broker = "tcp://broker.mqtt-dashboard.com:1883";
    private final String topic_temp = "IOT-Progetto-03-temp";
    private final String topic_control = "IOT-Progetto-03-per";


    private final Integer samplingNormal = 5000;
    private final float HOT_TEMP = 30;
    private final Integer samplingHot  = 1000;
    //private final float TOO_HOT_TEMP = 35;

    private final BlockingQueue<Float> temperatureQueue = new LinkedBlockingQueue<>();

    public MQTTTemperatureReceiver() throws MqttException {
        String clientId = "JavaMqttClient-" + System.currentTimeMillis();

        // Creazione e configurazione del client
        client = new MqttClient(broker, clientId, new MemoryPersistence());
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);

        // Configura il callback
        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                System.out.println("Connessione persa: " + cause.getMessage());
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                try {
                    String strValue = new String(message.getPayload());
                    float temperature = Float.parseFloat(strValue);
                    temperatureQueue.add(temperature);
                } catch (NumberFormatException e) {
                    System.out.println("Messaggio ricevuto non valido: " + new String(message.getPayload()));
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                // Non utilizzato per un subscriber
            }
        });

        // Connessione al broker e sottoscrizione al topic temperatura
        client.connect(options);
        client.subscribe(topic_temp, 0);
        System.out.println("Connesso e sottoscritto al topic: " + topic_temp);
    }

    public void controlTemperature() {
        try {
            Float temperature = temperatureQueue.poll(5000, TimeUnit.MILLISECONDS);
            if (temperature != null) {
                int newFrequency = determineFrequency(temperature);
                sendControlMessage(newFrequency);
            } else {
                System.out.println("Nessun dato di temperatura ricevuto nel tempo previsto.");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private int determineFrequency(float temperature) {
        if (temperature > HOT_TEMP) {
            return samplingHot; // Campionamento frequente
        } else {
            return samplingNormal; // Campionamento meno frequente
        }
    }

    private void sendControlMessage(int frequency) {
        try {
            String message = "cu:" + frequency;
            client.publish(topic_control, new MqttMessage(message.getBytes()));
            System.out.println("Inviato nuovo periodo di campionamento: " + frequency + " ms");
        } catch (MqttException e) {
            System.err.println("Errore durante l'invio del messaggio di controllo: " + e.getMessage());
        }
    }

    public void disconnect() {
        try {
            if (client != null && client.isConnected()) {
                client.disconnect();
                System.out.println("Disconnesso dal broker MQTT");
            }
        } catch (MqttException e) {
            System.err.println("Errore durante la disconnessione: " + e.getMessage());
        }
    }
/* 
    public static void main(String[] args) {
        try {
            MQTTTemperatureReceiver controller = new MQTTTemperatureReceiver();
            while (true) {
                controller.controlTemperature();
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }*/
}
