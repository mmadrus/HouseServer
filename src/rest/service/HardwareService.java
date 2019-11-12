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
    private String arudinoResponse;
    private boolean pauseThread = false;
    private String requestResponse = "";

    private HardwareService () {

        this.serialPort = SerialPort.getCommPort("/dev/cu.usbserial-A4001KMk");
        this.serialPort.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);
        connectedToPort();
        this.inputStream = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
        this.outputStream = serialPort.getOutputStream();
        start();
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

            while (serialPort.isOpen()) {


                /*arudinoResponse = inputStream.readLine();

                if (pauseThread) {
                    requestResponse = arudinoResponse;
                    System.out.println("Request response: " + arudinoResponse);
                    pauseThread = false;

                }

                System.out.println("Arduino response: " + arudinoResponse);*/

            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private boolean connectedToPort () {

        serialPort.openPort();

        try {

            Thread.sleep(5000);
        } catch (Exception e) {

            e.printStackTrace();
        }

        return serialPort.isOpen();
    }

    public String send (JSONObject jsonObject) {

        try {

            requestResponse = "";
            int i = jsonObject.getInt("command");
            outputStream.write(i);

            /* = true;

            while (requestResponse.equals("")) {
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
