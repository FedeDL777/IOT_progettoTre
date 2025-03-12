package controlunitsubsystem.impl;

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
    public Scanner postReceive(float temperature, String status, int level) {
        String jsonInputString = "{\"temperature\": " + temperature + ", \"status\": \"" + status + "\", \"level\": "
                + level + "}";

        try {
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

            try (java.util.Scanner scanner = new java.util.Scanner(conn.getInputStream(),
                    StandardCharsets.UTF_8.name())) {
                String responseBody = scanner.useDelimiter("\\A").next();
                System.out.println("Response: " + responseBody);
                return scanner;
            }
        } catch (Exception e) {
            System.err.println("Error in postReceive");
        }
        return new Scanner("");
    }

}
