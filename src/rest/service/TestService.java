package rest.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("users")
public class TestService {


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void getUsers() {

    }
}
