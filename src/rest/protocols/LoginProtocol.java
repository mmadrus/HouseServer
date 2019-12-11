package rest.protocols;

import org.json.JSONException;
import org.json.JSONObject;
import rest.database.Database;

import java.util.UUID;

public class LoginProtocol {

    public JSONObject setProtocolString (JSONObject protocolString) throws JSONException {
        JSONObject authenticateUser = new JSONObject();
        String id = UUID.randomUUID().toString();
        authenticateUser.put("username", protocolString.getString("username"));
        authenticateUser.put("password", protocolString.getString("password"));


        String dbResponse = Database.getInstance().loginMethod(authenticateUser.toString());

        JSONObject serverReply = new JSONObject();
        serverReply.put("token", id);
        serverReply.put("request-type", "authenticate-user");
        serverReply.put("result", dbResponse);

        return serverReply;
    }
}
