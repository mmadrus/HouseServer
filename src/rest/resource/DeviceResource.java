package rest.resource;

import rest.database.Database;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("service/device")
public class DeviceResource {

    @GET
    @Path("/update")
    public Response getUpdates () {

        return Response.ok("Device update").build();
    }

    @GET
    @Path("/newinstance")
    public Response getCurrentStatus () {

        return Response.ok("Device current status").build();
    }
}
