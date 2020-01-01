package rest.protocols;

import org.json.JSONException;
import org.json.JSONObject;
import rest.database.Database;

public class CommandProtocol {


    public JSONObject protocolCheck (JSONObject jsonObject) throws JSONException {

        JSONObject returnObject = new JSONObject();

        returnObject.put("token", jsonObject.getString("token"));
        returnObject.put("deviceId", jsonObject.getString("deviceId"));

        if (Database.getInstance().commandLog(jsonObject, 6)) {

            if (TokenProtocol.getInstance().isAlive(jsonObject.getString("token"))) {

                returnObject.put("result", 1);


            } else {

                returnObject.put("result", 0);
            }

        } else {

            returnObject.put("result", 0);
        }


        return returnObject;
    }
}
