package rest.service;

import com.fazecast.jSerialComm.SerialPort;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class HardwareService extends Thread {

    private SerialPort serialPort;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private String arudinoResponse;

    public HardwareService () {

        this.serialPort = SerialPort.getCommPort("");
        this.inputStream = new DataInputStream(serialPort.getInputStream());
        this.outputStream = new DataOutputStream(serialPort.getOutputStream());
        start();

    }

    @Override
    public void run () {

        try {

            while (connectedToPort()) {

                arudinoResponse = inputStream.readUTF();
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private boolean connectedToPort () {

        return serialPort.openPort();
    }
}
