package rest.service;

import com.fazecast.jSerialComm.SerialPort;
import org.json.JSONObject;

import java.io.*;
import java.nio.ByteBuffer;

public class HardwareService extends Thread {

    private static HardwareService hardwareService;
    private SerialPort serialPort;
    private BufferedReader inputStream;
    private OutputStream outputStream;
    private boolean pauseThread = false;
    private String requestResponse = "";

    private HardwareService () {

        this.serialPort = SerialPort.getCommPort("/dev/cu.usbserial-A4001KMk");
        //this.serialPort = SerialPort.getCommPort("/dev/cu.usbmodem14101");
        this.serialPort.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);
        if (connectedToPort()) {
            this.inputStream = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
            this.outputStream = serialPort.getOutputStream();
            start();
        }
    }

    public static HardwareService getInstance() {

        if (hardwareService == null) {

            hardwareService = new HardwareService();

        }

        return hardwareService;
    }

    @Override
    public void run () {

        try {

            String arduinoResponse;
            while (serialPort.isOpen()) {


                arduinoResponse = inputStream.readLine();

                if (pauseThread) {
                    requestResponse = arduinoResponse;
                    pauseThread = false;

                }

                System.out.println("Arduino response: " + arduinoResponse);

            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private boolean connectedToPort () {

        System.out.println(serialPort.getSystemPortName());
        serialPort.openPort();
        System.out.println(serialPort.getSystemPortName());

        try {

            Thread.sleep(3000);
        } catch (Exception e) {

            e.printStackTrace();
        }

        return serialPort.isOpen();
    }

    public String send (String jsonObject) {

        try {

            int i = Integer.parseInt(jsonObject);
            System.out.println("COMMAND: " + i);

            outputStream.write(i);


            /*pauseThread = true;

            while (pauseThread) {
                try {
                    Thread.sleep(100);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }*/

        } catch (Exception e) {

            e.printStackTrace();
        }


        return requestResponse;
    }

}
