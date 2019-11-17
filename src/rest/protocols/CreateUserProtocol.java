package rest.protocols;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.json.JSONTokener;
import rest.database.Database;

public class CreateUserProtocol {

    public JSONObject setProtocolString (JSONObject protocolString) {
        JSONObject newUser = new JSONObject();
        String id = UUID.randomUUID().toString();
        newUser.put("userName", protocolString.getString("userName"));
        newUser.put("email", protocolString.getString("email"));
        newUser.put("firstName", protocolString.getString("firstName"));
        newUser.put("lastName", protocolString.getString("lastName"));
        newUser.put("password", protocolString.getString("password"));
        newUser.put("userId", id);

        String dbResponse = Database.getInstance().createUser(newUser.toString());

        JSONObject serverReply = new JSONObject();
        serverReply.put("token", id);
        serverReply.put("request-type", "create-user");
        serverReply.put("result", dbResponse);

        return serverReply;
    }
}
