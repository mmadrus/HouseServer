package rest.protocols;

import org.json.JSONException;
import org.json.JSONObject;
import rest.database.Database;
import rest.resource.HardwareSocket;

public class CommandProtocol {


    public JSONObject protocolCheck(JSONObject jsonObject) throws JSONException {

        JSONObject returnObject = new JSONObject();

        returnObject.put("token", jsonObject.getString("token"));
        returnObject.put("deviceId", jsonObject.getString("deviceId"));

        if (TokenProtocol.getInstance().isAlive(jsonObject.getString("token"))) {

            try {

                String id = String.valueOf(jsonObject.getInt("deviceId"));
                String command = String.valueOf(jsonObject.getInt("command"));
                System.out.println(id + command);
                HardwareSocket.broadcast(id + command);
                JSONObject temp = Database.getInstance().changeDeviceState(jsonObject);
                returnObject.put("result", temp.getInt("result"));

            } catch (Exception e) {

                e.printStackTrace();
                returnObject.put("result", 0);
            }



        } else {

            returnObject.put("result", 0);
        }

        Database.getInstance().commandLog(jsonObject);

        return returnObject;
    }
}
