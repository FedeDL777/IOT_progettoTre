import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public class HttpClient {
    public static void main(String[] args) {
        String serverUrl = "http://127.0.0.1:5000/send"; // URL del server

        // JSON da inviare
        String jsonData = "{\"temperature\": \"22.5\", \"status\": \"NORMAL\"}";

        try {
            // Creazione della connessione
            URI url = new URI(serverUrl);
            HttpURLConnection conn = (HttpURLConnection) url.toURL().openConnection();

            // Configurazione della richiesta
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Scrittura dei dati JSON nella richiesta
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonData.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Lettura della risposta
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Messaggio inviato con successo!");
            } else {
                System.out.println("Errore: " + responseCode);
            }

            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
