package rest.resource;

import org.json.JSONArray;
import org.json.JSONObject;
import rest.database.Database;
import rest.protocols.TokenProtocol;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@SuppressWarnings("DuplicatedCode")
@Path("service/param/house")
@Consumes(MediaType.TEXT_PLAIN)
@Produces(MediaType.TEXT_PLAIN)
public class HouseParamResource {

    @GET
    @Path("/room/{username}/{token}/{roomID}")
    public Response getRoomDevices (@PathParam("username") String username, @PathParam("token") String token,
                                    @PathParam("roomID") int roomID) {

        JSONObject jsonObject = new JSONObject().put("username", username).put("token", token).put("roomID", roomID);
        Database.getInstance().commandLog(jsonObject);
        JSONObject object = new JSONObject();
        if (TokenProtocol.getInstance().isAlive(jsonObject.getString("token"))) {

            object = Database.getInstance().getRoomDevices(jsonObject);

        } else {

            object = new JSONObject().put("result", "fail");
        }

        return Response.ok(object.toString(1)).build();
    }

    @GET
    @Path("/{username}/{token}/{houseID}")
    public Response getHouseRooms (@PathParam("username") String username, @PathParam("token") String token,
                                   @PathParam("houseID") int houseID) {

        JSONObject jsonObject = new JSONObject().put("username", username).put("token", token).put("houseID", houseID);
        Database.getInstance().commandLog(jsonObject);
        JSONObject object = new JSONObject();
        if (TokenProtocol.getInstance().isAlive(jsonObject.getString("token"))) {

            object = Database.getInstance().getHouseRooms(jsonObject);

        } else {

            object = new JSONObject().put("result", "fail");
        }

        return Response.ok(object.toString(1)).build();
    }

    @GET
    @Path("/{username}/{token}")
    public Response getUserHouses (@PathParam("username") String username, @PathParam("token") String token) {

        JSONObject jsonObject = new JSONObject().put("username", username).put("token", token);
        System.out.println(jsonObject.toString());
        Database.getInstance().commandLog(jsonObject);
        JSONArray object = null;
        if (TokenProtocol.getInstance().isAlive(jsonObject.getString("token"))) {

            object = Database.getInstance().getUserHouses(jsonObject);

        } else {

            object = new JSONArray().put(new JSONObject().put("result", 0));
        }

        return Response.ok(object.toString(1)).build();
    }
}
