package rest.resource;

import org.json.JSONArray;
import org.json.JSONObject;
import rest.database.Database;
import rest.protocols.TokenProtocol;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@SuppressWarnings("DuplicatedCode")
@Path("service/param/update")
@Consumes (MediaType.TEXT_PLAIN)
@Produces(MediaType.TEXT_PLAIN)
public class DeviceParamResource {

    @GET
    @Path("/device/{username}/{token}/{roomID}")
    public Response getDeviceUpdate (@PathParam("username") String username, @PathParam("token") String token,
                                     @PathParam("roomID") int roomID) {

        JSONObject jsonObject = new JSONObject().put("username", username).put("token", token).put("roomID", roomID);
        JSONArray jsonArray = null;
        if (TokenProtocol.getInstance().isAlive(jsonObject.getString("token"))) {

            jsonArray = Database.getInstance().getDeviceUpdate();

        } else {

            jsonArray = new JSONArray().put(new JSONObject().put("result", 0));
        }

        //Database.getInstance().commandLog(jsonObject.put("request", "getDevice"));

        return Response.ok(jsonArray.toString()).build();

    }

    @GET
    @Path("/alarm/{username}/{token}/{houseID}")
    public Response getAlarmUpdate (@PathParam("username") String username, @PathParam("token") String token,
                                     @PathParam("houseID") int houseID) {

        JSONObject jsonObject = new JSONObject().put("username", username).put("token", token).put("houseID", houseID);
        JSONArray jsonArray = null;
        if (TokenProtocol.getInstance().isAlive(jsonObject.getString("token"))) {

            jsonArray = Database.getInstance().getAlarmUpdate();

        } else {

            jsonArray = new JSONArray().put(new JSONObject().put("result", 0).put("fail", "token"));
        }

        //Database.getInstance().commandLog(jsonObject.put("request", "getAlarm"));

        return Response.ok(jsonArray.toString()).build();

    }

    @GET
    @Path("/sensor/{username}/{token}/{houseID}")
    public Response getSensorUpdate (@PathParam("username") String username, @PathParam("token") String token,
                                    @PathParam("houseID") int houseID) {

        JSONObject jsonObject = new JSONObject().put("username", username).put("token", token).put("houseID", houseID);

        JSONObject jsonArray = null;
        if (TokenProtocol.getInstance().isAlive(jsonObject.getString("token"))) {

            jsonArray = Database.getInstance().getSensorUpdate();

        } else {

            jsonArray = new JSONObject().put("result", 0).put("fail", "token");
        }

        //Database.getInstance().commandLog(jsonObject.put("request", "getSensor"));

        return Response.ok(jsonArray.toString()).build();

    }

    @DELETE
    @Path("/delete/{username}/{token}/{deviceID}")
    public Response deleteDevice (@PathParam("deviceID") int deviceID, @PathParam("token") String token,
                                  @PathParam("username") String username) {

        JSONObject jsonObject = new JSONObject().put("deviceID", deviceID).put("token", token).put("username", username);
        JSONObject jsonArray = null;
        if (TokenProtocol.getInstance().isAlive(jsonObject.getString("token"))) {

            jsonArray = Database.getInstance().deleteDevice(jsonObject);

        } else {

            jsonArray = new JSONObject().put("result", 0);
        }

        //Database.getInstance().commandLog(jsonObject.put("request", "deleteDevice"));

        return Response.ok(jsonArray.toString()).build();
    }
}
