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
import rest.models.Token;

public class CreateUserProtocol {

    public JSONObject setProtocolString (JSONObject newUser) throws JSONException {

        String id = UUID.randomUUID().toString();
        newUser.put("userId", id);

        JSONObject dbResponse = Database.getInstance().createUser(newUser);

        return dbResponse;
    }
}
