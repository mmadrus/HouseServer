package rest.protocols;

import org.json.JSONObject;
import rest.database.Database;

import java.util.UUID;

public class LoginProtocol {

    public JSONObject setProtocolString (JSONObject user) {

            JSONObject dbResponse = Database.getInstance().loginMethod(user);
            dbResponse.put("username", user.getString("username"));

            return dbResponse;


    }
}
