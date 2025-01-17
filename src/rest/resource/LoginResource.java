package rest.resource;


import org.glassfish.jersey.server.ResourceConfig;
import org.json.JSONObject;
import org.json.JSONTokener;
import rest.database.Database;
import rest.models.Token;
import rest.protocols.JSONProtocol;
import rest.protocols.LoginProtocol;
import rest.protocols.TokenProtocol;
import rest.resource.AuthenticationHandler;
import rest.utils.AuthUtils;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("service/user")
public class LoginResource {

    private LoginProtocol loginProtocol = new LoginProtocol();

    @PUT
    @Path("/login")
    public Response authenticateUser(String json) {

        JSONObject authenticateUser = JSONProtocol.getInstance().toJson(json);

        JSONObject dbResponse = loginProtocol.setProtocolString(authenticateUser);

        Database.getInstance().commandLog(authenticateUser.put("request", "login"));


        return Response.ok(dbResponse.toString()).build();

    }
}

