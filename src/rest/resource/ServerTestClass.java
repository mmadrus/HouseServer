package rest.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.OK;

@Path("users")
@Produces(MediaType.APPLICATION_JSON)
public class ServerTestClass {

    AuthenticationHandler authService;

    @GET
    @Path("/{userId}")
    public Response getUserWithName(@PathParam("userId") String name) {
        Object result = authService.getUserWithName();
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

    @GET
    public Response getUsers() {
        authService = new AuthenticationHandler();
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
