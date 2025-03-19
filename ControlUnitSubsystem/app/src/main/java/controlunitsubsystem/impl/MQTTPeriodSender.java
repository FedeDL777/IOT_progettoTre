package controlunitsubsystem.impl;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MQTTPeriodSender {
    private MqttClient client;
    private final String broker = "tcp://broker.mqtt-dashboard.com:1883";
    private final String topic = "IOT-Progetto-03-period";

    /**
     * Inizializza e connette il sender MQTT.
     *
     * @throws MqttException se si verifica un errore nella connessione MQTT
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
                // Non utilizzato per un publisher
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
     * Invia un valore di periodo come intero al topic MQTT.
     * 
     * @param period il valore del periodo da inviare
     * @throws MqttException se si verifica un errore nell'invio del messaggio
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