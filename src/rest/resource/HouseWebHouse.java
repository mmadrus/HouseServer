package rest.resource;


import org.json.JSONObject;
import rest.database.Database;
import rest.protocols.JSONProtocol;
import rest.protocols.TokenProtocol;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("service/web/house")
@Consumes(MediaType.TEXT_PLAIN)
@Produces(MediaType.TEXT_PLAIN)
public class HouseWebHouse {

    @GET
    public Response test () {

        return Response.ok("WTF").build();
    }

    @GET
    @Path("/{username}/{token}/{houseName}")
    public Response getHouses (@PathParam("username") String username, @PathParam("token") String token,
                               @PathParam("houseName") String housename) {

        JSONObject jsonObject = new JSONObject().put("username", username).put("token", token).put("houseName", housename.replace("-", " "));
        Database.getInstance().commandLog(jsonObject);
        JSONObject object = null;
        if (TokenProtocol.getInstance().isAlive(jsonObject.getString("token"))) {

            object = Database.getInstance().getSpecificHouse(jsonObject);

        } else {

            object = new JSONObject().put("result", 0);
        }

        return Response.ok(object.toString()).build();
    }

    @GET
    @Path("/room/{username}/{token}/{roomID}")
    public Response getRooms (@PathParam("username") String username, @PathParam("token") String token,
                              @PathParam("roomID") int roomID) {

        JSONObject jsonObject = new JSONObject().put("username", username).put("token", token).put("roomID", roomID);
        Database.getInstance().commandLog(jsonObject);
        JSONObject object = new JSONObject();
        if (TokenProtocol.getInstance().isAlive(jsonObject.getString("token"))) {

            object = Database.getInstance().getHouseRooms(jsonObject);

        } else {

            object = new JSONObject().put("result", "fail");
        }

        return Response.ok(object.toString()).build();
    }
}
