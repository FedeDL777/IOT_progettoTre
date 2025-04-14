package controlunitsubsystem;

import controlunitsubsystem.impl.ControlUnitImpl;

import controlunitsubsystem.api.ControlUnit.Status;

import controlunitsubsystem.api.ControlUnit;

public class App {
    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) {
        System.out.println(new App().getGreeting());
        String comPortName = SerialPortDetector.findArduinoPort();
        final int P1 = 5000;
        final int P2 = 1000;
        int period = P1;
        //long lastTime;
        Status status = Status.NORMAL;

        ControlUnit controlUnit = new ControlUnitImpl("http://127.0.0.1:5000/send", comPortName);

        controlUnit.sendPeriod(period);
        while (true) {
            //lastTime = System.currentTimeMillis();
            status = controlUnit.updateMotorAndStatusTick(period);
            if (status == Status.NORMAL) {
                if (period != P1) {
                    period = P1;
                    controlUnit.sendPeriod(period);
                }
            } else {
                if (period != P2) {
                    period = P2;
                    controlUnit.sendPeriod(period);
                }
            }
            controlUnit.dashboardTick();
        }

        // controlUnit.destroy();
    }
}
