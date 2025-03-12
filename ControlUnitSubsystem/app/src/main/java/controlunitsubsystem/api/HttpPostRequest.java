package controlunitsubsystem.api;

import java.util.Scanner;

public interface HttpPostRequest {

    Scanner postReceive(float temperature, String status, int level);
}
