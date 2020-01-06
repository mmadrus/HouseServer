package rest.resource;

import org.json.JSONException;
import org.json.JSONObject;
import rest.database.Database;
import rest.protocols.JSONProtocol;
import rest.protocols.TokenProtocol;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("service/web/house")
public class HouseWebHouse {

    @GET
    @Path("/{username}/{token}/{houseName}")
    public Response getHouses (@PathParam("username") String username, @PathParam("token") String token,
                               @PathParam("houseName") String housename) throws JSONException {

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
                              @PathParam("roomID") int roomID) throws JSONException {

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
