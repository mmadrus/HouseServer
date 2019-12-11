package rest.protocols;

import org.json.JSONException;
import org.json.JSONObject;
import rest.database.Database;

public class CommandProtocol {

    private TokenProtocol tokenProtocol = new TokenProtocol();

    public JSONObject protocolCheck (JSONObject jsonObject) throws JSONException {

        JSONObject returnObject = new JSONObject();

        returnObject.put("token", jsonObject.getString("token"));
        returnObject.put("requestType", jsonObject.getString("requestType"));
        returnObject.put("deviceId", jsonObject.getInt("deviceId"));

        if (Database.getInstance().commandLog(jsonObject)) {

            if (tokenProtocol.isAlive(jsonObject.getString("token"))) {

                    //returnObject.put("result", HardwareService.getInstance().send(jsonObject));
                if (jsonObject.getInt("command") == 2) {

                    returnObject.put("result", 2);
                    //PublishService.getInstance().publish(returnObject.toString());

                } else {
                    returnObject.put("result", jsonObject.getInt("command"));
                    //PublishService.getInstance().publish(returnObject.toString());
                }

                    return returnObject;
            }

        }

        returnObject.put("result", 0);

        return returnObject;
    }
}
