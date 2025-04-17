package controlunitsubsystem.impl;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MQTTPeriodSender {
    private MqttClient client;
    private final String broker = "tcp://test.mosquitto.org:1883";
    private final String topic = "IOT-Progetto-03-per";

    /**
     * Initialize and connects MQTT receiver.
     * 
     * @throws MqttException if occurs an error in the MQTT's connection
     */
    public MQTTPeriodSender() throws MqttException {
        String clientId = "JavaMqttPublisher-" + System.currentTimeMillis();
        // Creazione e configurazione del client
        client = new MqttClient(broker, clientId, new MemoryPersistence());
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);

        // Configura il callback per la connessione
        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                System.out.println("Connessione persa: " + cause.getMessage());
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                // Non utilizzato in questo contesto
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                System.out.println("Messaggio consegnato");
            }
        });

        // Connessione al broker
        client.connect(options);
        System.out.println("Connesso al broker MQTT: " + broker);
    }

    /**
     * Sends a period value as an integer.
     * 
     * @param period the value to be sent
     * @throws MqttException if occurs an error in sending the message
     */
    public void sendPeriod(int period) throws MqttException {
        if (client != null && client.isConnected()) {
            String periodString = String.valueOf(period);
            MqttMessage message = new MqttMessage(periodString.getBytes());
            message.setQos(0);
            client.publish(topic, message);
            System.out.println("Periodo inviato: " + period);
        } else {
            System.err.println("Client non connesso. Impossibile inviare il periodo.");
        }
    }

    /**
     * Closes MQTT's connection.
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