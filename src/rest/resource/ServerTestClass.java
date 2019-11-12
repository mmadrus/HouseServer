package rest.resource;

import rest.database.Database;
import rest.service.AuthServiceImpl;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.OK;

@Path("users")
@Produces(MediaType.APPLICATION_JSON)
public class ServerTestClass {

    AuthServiceImpl authService;
    Database database = Database.getInstance();


    @GET
    @Path("test")
    public String getString() {

        return "Hello world";

    }
    @GET
    public Response getUsers() {
        Object result = authService.getUsers();
        if (result != null) {
            GenericEntity<Object> genericEntity = new GenericEntity<Object>(result) {
            };
            return Response.status(OK)
                        .entity(genericEntity)
                        .build();
        } else {
            return Response.status(NOT_FOUND)
                        .entity(null)
                        .build();
        }

    }
}
