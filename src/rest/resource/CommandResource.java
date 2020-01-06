package rest.resource;

import org.json.JSONException;
import org.json.JSONObject;
import rest.database.Database;
import rest.protocols.CommandProtocol;
import rest.protocols.JSONProtocol;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("service/device")
public class CommandResource {

    private CommandProtocol commandProtocol = new CommandProtocol();

    @PUT
    @Path("/command")
    public Response userRequest (String jsonString) throws JSONException {

        JSONObject jsonObject = JSONProtocol.getInstance().toJson(jsonString);
        JSONObject obj = commandProtocol.protocolCheck(jsonObject);

        return Response.ok(obj.toString()).build();
    }

}
