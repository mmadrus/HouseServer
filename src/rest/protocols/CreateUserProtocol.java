package rest.protocols;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

import com.google.gson.JsonObject;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.json.JSONTokener;
import rest.database.Database;

public class CreateUserProtocol {

    public JSONObject setProtocolString (JSONObject protocolString) throws JSONException {
        JSONObject newUser = new JSONObject();
        String id = UUID.randomUUID().toString();
        newUser.put("username", protocolString.getString("username"));
        newUser.put("email", protocolString.getString("email"));
        newUser.put("firstName", protocolString.getString("firstName"));
        newUser.put("lastName", protocolString.getString("lastName"));
        newUser.put("password", protocolString.getString("password"));
        newUser.put("userId", id);

        JSONObject dbResponse = Database.getInstance().createUser(newUser);

        JSONObject serverReply = new JSONObject();
        serverReply.put("token", id);
        serverReply.put("request-type", "create-user");
        serverReply.put("result", dbResponse);

        return serverReply;
    }
}
