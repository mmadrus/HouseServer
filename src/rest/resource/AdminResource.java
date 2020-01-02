package rest.resource;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("admin")
public class AdminResource {

    @PUT
    public Response checkLogin (String json) {

        System.out.println(json);
        System.out.println("HEEEEJ");
        return Response.ok().build();
    }
}
