package rest.protocols;

public class HardwareMessageProtocol {

    public void decodeMessage (String msg) {

        String command = msg.substring(msg.length() - 1);
        String id = msg.substring(0, msg.length() - 1);


    }
}
