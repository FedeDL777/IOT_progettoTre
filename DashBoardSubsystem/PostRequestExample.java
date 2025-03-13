import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class PostRequestExample {
    public static void main(String[] args) {
        try {
            String serverUrl = "http://127.0.0.1:5000/send";
            String jsonInputString = "{\"temperature\": \"22.5\", \"status\": \"HOT\", \"level\": \"75\"}";

            URI url = new URI(serverUrl);
            HttpURLConnection conn = (HttpURLConnection) url.toURL().openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            try (Scanner scanner = new Scanner(conn.getInputStream(),
                    StandardCharsets.UTF_8.name())) {
                //String responseBody = scanner.useDelimiter("\\A").next();
                System.out.println("Response: " + scanner.nextFloat());
            }

            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
