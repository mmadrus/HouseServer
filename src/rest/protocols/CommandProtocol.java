package rest.protocols;

import org.json.JSONObject;
import rest.database.Database;
import rest.service.HardwareService;

public class CommandProtocol {

    private TokenProtocol tokenProtocol = new TokenProtocol();

    public JSONObject protocolCheck (JSONObject jsonObject) {

        JSONObject returnObject = new JSONObject();

        returnObject.put("token", jsonObject.getString("token"));
        returnObject.put("request-type", jsonObject.getString("request-type"));
        returnObject.put("device-id", jsonObject.getString("device-id"));

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
