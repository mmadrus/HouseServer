package rest.resource;

import org.json.JSONObject;
import rest.database.Database;
import rest.protocols.JSONProtocol;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("admin")
public class AdminResource {

    @PUT
    public Response checkLogin (String json) {

        JSONObject jsonObject = JSONProtocol.getInstance().toJson(json);

        return Response.ok(Database.getInstance().adminLogin(jsonObject).toString()).build();
    }
}
