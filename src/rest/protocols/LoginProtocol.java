package rest.protocols;

import org.json.JSONException;
import org.json.JSONObject;
import rest.database.Database;

import java.util.UUID;

public class LoginProtocol {

    public JSONObject setProtocolString (JSONObject user) throws JSONException {

        if (TokenProtocol.getInstance().isAlive(user.getString("token"))) {
            JSONObject dbResponse = Database.getInstance().loginMethod(user);
            dbResponse.put("token", user.getString("token"));
            dbResponse.put("username", user.getString("username"));

            return dbResponse;

        }

        return new JSONObject().put("result", 0).put("token", user.getString("token"))
                                .put("username", user.getString("username")).put("basj", "bajs");
    }
}
