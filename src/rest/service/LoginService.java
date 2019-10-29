package rest.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("login")
public class LoginService {

    @GET
    @Path("/ok")
    public String ok () {

        return "ok";
    }
}
