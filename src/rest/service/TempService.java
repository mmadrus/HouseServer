package rest.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/service")
public class TempService {

    @GET
    public String getString () {

        return "Hej";
    }
}
