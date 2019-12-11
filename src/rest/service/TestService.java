package rest.service;

import rest.resource.AuthenticationHandler;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.OK;

@Path("users")
public class TestService {

    AuthenticationHandler authService;

    @Path("test")
    @GET
    public String getString() {

        return "penis";

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
