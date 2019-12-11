package rest.service;


import org.glassfish.jersey.server.ResourceConfig;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import rest.database.Database;
import rest.models.Token;
import rest.protocols.LoginProtocol;
import rest.resource.AuthenticationHandler;
import rest.utils.AuthUtils;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/authenticate")
public class LoginService {

    AuthenticationHandler authService;
    Database database;

    LoginProtocol loginProtocol = new LoginProtocol();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String authenticateUser(String json) throws JSONException {

        database = Database.getInstance();
        AuthUtils authUtils = new AuthUtils();
        authService = new AuthenticationHandler();
        // try {
        JSONObject authenticateUser = new JSONObject(new JSONTokener(json));


        Token token = new Token();

        return loginProtocol.setProtocolString(authenticateUser).toString();

      /*      return Response.ok(token).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }*/
    }
}

