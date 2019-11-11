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

    public String setProtocolString (String protocolString) {
        JSONObject newUser = new JSONObject(new JSONTokener(protocolString));
        String id = UUID.randomUUID().toString();
        newUser.put("ID", id);
        Database db = new Database();

        db.createUser(newUser);

        JSONObject serverReply = new JSONObject();
        serverReply.put("token", "server-generated token");
        serverReply.put("request-type", "create-user");
        serverReply.put("result", "1");
        String reply = serverReply.toString();

        /*try {
            BufferedWriter out = new BufferedWriter(new FileWriter("newUser.json"));
            out.write(newUser.toString());
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        return reply;
    }
}
