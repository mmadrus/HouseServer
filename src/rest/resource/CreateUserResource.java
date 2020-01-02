package rest.resource;

import com.google.gson.JsonObject;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.json.JSONTokener;
import rest.database.Database;
import rest.protocols.CreateUserProtocol;
import rest.protocols.JSONProtocol;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Path("service/user")
public class CreateUserResource {

    private CreateUserProtocol userProtocol = new CreateUserProtocol();

    @Path("/create")
    @POST
    public Response newUser(String json) throws JSONException {


        // newUser is the JSON that has the information from the client and should be sent to the
        JSONObject newUser = JSONProtocol.getInstance().toJson(json);
        JSONObject serverResponse = userProtocol.setProtocolString(newUser);
        newUser.remove("password");
        Database.getInstance().commandLog(newUser);

        return Response.ok(serverResponse.toString()).build();
    }



}
