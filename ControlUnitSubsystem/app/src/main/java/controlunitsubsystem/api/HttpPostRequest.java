package controlunitsubsystem.api;

import java.io.IOException;

public interface HttpPostRequest {

    String postReceive(float temperature, String status, int level) throws IOException;
}
