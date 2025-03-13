package controlunitsubsystem.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import controlunitsubsystem.api.HttpPostRequest;

public class HttpPostRequestImpl implements HttpPostRequest {
    URL url;

    public HttpPostRequestImpl(String url) {
        try {
            this.url = new URI(url).toURL();
        } catch (Exception e) {
            System.err.println("Invalid URL in HttpPostRequestImpl");
        }
    }

    @Override
    public String postReceive(float temperature, String status, int level) throws IOException {
        String jsonInputString = "{\"temperature\": " + temperature + ", \"status\": \"" + status + "\", \"level\": "
                + level + "}";

        
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
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
                String responseBody = scanner.useDelimiter("\\A").next();
                System.out.println("Response: " + responseBody);
                return responseBody;
            }
        
    }

}
