package rest.protocols;

import org.json.JSONObject;
import rest.database.Database;

public class HardwareMessageProtocol {

    public void decodeMessage (String msg) {

        int command;
        int id;

        if (msg.substring(0,1).equals("C")) {
            command = Integer.parseInt(msg.substring(msg.length() - 2));
            id = Integer.parseInt(msg.substring(1, msg.length() - 2));
        } else if (msg.substring(0,1).equals("E")){
            command = Integer.parseInt(msg.substring(6));
            id = Integer.parseInt(msg.substring(1, 6));
        } else {

            command = Integer.parseInt(msg.substring(msg.length() - 1));
            id = Integer.parseInt(msg.substring(0, msg.length() - 1));
        }

        Database.getInstance().updateDeviceStatus(new JSONObject().put("deviceID", id).put("command", command));
    }
}
