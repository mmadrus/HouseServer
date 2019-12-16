package rest.resource;

import org.json.JSONArray;
import rest.database.Database;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("service/device")
public class DeviceResource {

    @GET
    @Path("/update")
    public Response getUpdates () {



        return Response.ok("Get Update").build();
    }

    @GET
    @Path("/newinstance")
    public Response getCurrentStatus () {

        JSONArray jsonArray = Database.getInstance().getDevices();

        return Response.ok(jsonArray.toString()).build();
    }
}
