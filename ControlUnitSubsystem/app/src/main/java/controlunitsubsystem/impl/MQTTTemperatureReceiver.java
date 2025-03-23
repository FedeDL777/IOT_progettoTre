package controlunitsubsystem.impl;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class MQTTTemperatureReceiver {
    private MqttClient client;
    private final String broker = "tcp://broker.mqtt-dashboard.com:1883";
    private final String topic = "IOT-Progetto-03";
    private final BlockingQueue<Float> temperatureQueue = new LinkedBlockingQueue<>();

    /**
     * Inizializza e connette il ricevitore MQTT.
     * 
     * @throws MqttException se si verifica un errore nella connessione MQTT
     */
    public MQTTTemperatureReceiver() throws MqttException {
        String clientId = "JavaMqttClient-" + System.currentTimeMillis();

        // Creazione e configurazione del client
        client = new MqttClient(broker, clientId, new MemoryPersistence());
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);

        // Configura il callback per i messaggi
        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                System.out.println("Connessione persa: " + cause.getMessage());
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                try {
                    // Converte il messaggio in float e lo mette nella coda
                    String strValue = new String(message.getPayload());
                    float temperature = Float.parseFloat(strValue);
                    temperatureQueue.add(temperature);
                } catch (NumberFormatException e) {
                    System.out.println("Messaggio ricevuto non è un float valido: " + new String(message.getPayload()));
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                // Non utilizzato per un subscriber
            }
        });

        // Connessione al broker
        client.connect(options);

        // Sottoscrizione al topic
        client.subscribe(topic, 0);
        System.out.println("Connesso e sottoscritto al topic: " + topic);
    }

    /**
     * Riceve un valore di temperatura dal topic MQTT.
     * Attende fino a quando non arriva una temperatura o fino al timeout.
     * 
     * @param timeoutMillis millisecondi di attesa prima di ritornare null
     * @return il valore di temperatura o null se si è verificato un timeout
     */
    public Float receiveTemperature(int timeoutMillis) {
        try {
            return temperatureQueue.poll(timeoutMillis, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }

    /**
     * Chiude la connessione MQTT.
     */
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
}
