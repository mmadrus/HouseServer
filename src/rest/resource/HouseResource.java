package rest.resource;

import com.google.gson.JsonObject;
import org.glassfish.jersey.internal.util.Property;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.html.HTMLBodyElement;
import rest.database.Database;
import rest.protocols.JSONProtocol;
import rest.protocols.TokenProtocol;

import javax.annotation.security.PermitAll;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@SuppressWarnings("DuplicatedCode")
@Path("service/house")
public class HouseResource {

    @GET
    public Response getHouses (String json) {

        JSONObject jsonObject = JSONProtocol.getInstance().toJson(json);
        Database.getInstance().commandLog(jsonObject);
        JSONObject object = null;
        if (TokenProtocol.getInstance().isAlive(jsonObject.getString("token"))) {

            object = Database.getInstance().getSpecificHouse(jsonObject);

        } else {

            object = new JSONObject().put("result", 0);
        }

        return Response.ok(object.toString(1)).build();
    }

    @GET
    @Path("/room")
    public Response getRooms (String json) {

        JSONObject jsonObject = JSONProtocol.getInstance().toJson(json);
        Database.getInstance().commandLog(jsonObject);
        JSONObject object = new JSONObject();
        if (TokenProtocol.getInstance().isAlive(jsonObject.getString("token"))) {

            object = Database.getInstance().getHouseRooms(jsonObject);

        } else {

            object = new JSONObject().put("result", "fail");
        }

        return Response.ok(object.toString()).build();
    }

    @GET
    @Path("/room/device")
    public Response getDevices (String json) {

        JSONObject jsonObject = JSONProtocol.getInstance().toJson(json);
        Database.getInstance().commandLog(jsonObject);
        JSONObject object = new JSONObject();
        if (TokenProtocol.getInstance().isAlive(jsonObject.getString("token"))) {

            object = Database.getInstance().getRoomDevices(jsonObject);

        } else {

            object = new JSONObject().put("result", "fail");
        }

        return Response.ok(object.toString()).build();
    }

    @POST
    public Response addHouse (String json) {

        JSONObject jsonObject = JSONProtocol.getInstance().toJson(json);
        Database.getInstance().commandLog(jsonObject);
        JSONObject object = null;
        if (TokenProtocol.getInstance().isAlive(jsonObject.getString("token"))) {

            object = Database.getInstance().createNewHouse(jsonObject);

        } else {

            object.put("resource", "fail");
            object = new JSONObject().put("result", 0);
        }

        return Response.ok(object.toString()).build();
    }

    @POST
    @Path("/room")
    public Response addRoom (String json) {

        JSONObject jsonObject = JSONProtocol.getInstance().toJson(json);
        Database.getInstance().commandLog(jsonObject);
        JSONObject object = null;
        if (TokenProtocol.getInstance().isAlive(jsonObject.getString("token"))) {

            object = Database.getInstance().createNewRoom(jsonObject);

        } else {

            object = new JSONObject().put("result", 0);
        }

        return Response.ok(object.toString()).build();
    }

    @POST
    @Path("/device")
    public Response addDevice (String json) {

        JSONObject jsonObject = JSONProtocol.getInstance().toJson(json);
        Database.getInstance().commandLog(jsonObject);
        JSONObject object = null;
        if (TokenProtocol.getInstance().isAlive(jsonObject.getString("token"))) {

            object = Database.getInstance().createNewDevice(jsonObject);

        } else {

            object = new JSONObject().put("result", 0);
        }


        return Response.ok(object.toString()).build();
    }

    @PUT
    @Path("/access")
    public Response getAccess (String json) {


        JSONObject jsonObject = JSONProtocol.getInstance().toJson(json);
        Database.getInstance().commandLog(jsonObject);
        JSONObject object = null;
        if (TokenProtocol.getInstance().isAlive(jsonObject.getString("token"))) {

            object = Database.getInstance().addAccessToHouse(jsonObject);

        } else {

            object = new JSONObject().put("result", 0);
        }

        return Response.ok(object.toString()).build();
    }

    @DELETE
    @Path("/device")
    public Response deleteDevice (String json) {

        JSONObject jsonObject = JSONProtocol.getInstance().toJson(json);
        Database.getInstance().commandLog(jsonObject);
        JSONObject object = null;
        if (TokenProtocol.getInstance().isAlive(jsonObject.getString("token"))) {

            object = Database.getInstance().deleteDevice(jsonObject);

        } else {

            object.put("resource", "fail");
            object = new JSONObject().put("result", 0);
        }

        return Response.ok(object.toString()).build();
    }
}
