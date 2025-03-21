
package controlunitsubsystem;

import com.fazecast.jSerialComm.SerialPort;

public class SerialPortDetector {
    public static String findArduinoPort() {
        SerialPort[] ports = SerialPort.getCommPorts();
        for (SerialPort port : ports) {
            System.out.println("Trovata porta: " + port.getSystemPortName());
            if (port.getDescriptivePortName().toLowerCase().contains("arduino") ||
                port.getDescriptivePortName().toLowerCase().contains("ch340") || 
                port.getDescriptivePortName().toLowerCase().contains("usb-serial")) {
                return port.getSystemPortName();
            }
        }
        return null;
    }

}
