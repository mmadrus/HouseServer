package rest.protocols;

import org.json.JSONObject;
import rest.database.Database;
import rest.service.HardwareService;

public class CommandProtocol {

    private TokenProtocol tokenProtocol = new TokenProtocol();

    public JSONObject protocolCheck (JSONObject jsonObject) {

        JSONObject returnObject = new JSONObject();

        returnObject.put("token", jsonObject.getString("token"));
        returnObject.put("requestType", jsonObject.getString("requestType"));
        returnObject.put("deviceId", jsonObject.getInt("deviceId"));

        //if (Database.getInstance().commandLog(jsonObject)) {

            if (tokenProtocol.isAlive(jsonObject.getString("token"))) {

                    returnObject.put("result", HardwareService.getInstance().send(jsonObject));

                    return returnObject;
            }

        //}

        returnObject.put("result", 0);

        return returnObject;
    }
}
