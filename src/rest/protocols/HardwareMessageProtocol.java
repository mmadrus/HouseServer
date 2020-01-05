package rest.protocols;

import org.json.JSONObject;
import rest.database.Database;

public class HardwareMessageProtocol {

    public void decodeMessage (String msg) {

        int command = Integer.parseInt(msg.substring(msg.length() - 1));
        int id = Integer.parseInt(msg.substring(0, msg.length() - 1));

        Database.getInstance().updateDeviceStatus(new JSONObject().put("deviceID", id).put("command", command));
    }
}
