package rest.protocols;


import org.json.JSONObject;
import rest.database.Database;
import rest.resource.HardwareSocket;

public class CommandProtocol {


    public JSONObject protocolCheck(JSONObject jsonObject) {

        JSONObject returnObject = new JSONObject();

        returnObject.put("token", jsonObject.getString("token"));
        returnObject.put("deviceID", jsonObject.getInt("deviceID"));

        if (TokenProtocol.getInstance().isAlive(jsonObject.getString("token"))) {

            try {

                String id = String.valueOf(jsonObject.getInt("deviceID"));
                String command = String.valueOf(jsonObject.getInt("command"));
                System.out.println(id + command);
                HardwareSocket.broadcast(id + command);
                JSONObject temp = Database.getInstance().updateDeviceStatus(jsonObject);
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
