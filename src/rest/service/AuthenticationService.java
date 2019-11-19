package rest.service;

//import org.glassfish.jersey.media.multipart.FormDataParam;
//import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import rest.database.Database;
import rest.models.Token;
import rest.resource.AuthenticationHandler;
import rest.utils.AuthUtils;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/authentication")
public class AuthenticationService extends ResourceConfig {

    AuthenticationHandler authService;
    Database database;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response authenticateUser(@FormParam("username") String username,
                                     @FormParam("password") String password) {

        database = Database.getInstance();
        AuthUtils authUtils = new AuthUtils();
        authService = new AuthenticationHandler();


        try {
            authService.authenticate(username, password);

            Token token = new Token();

            return  Response.ok(token).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }
}
