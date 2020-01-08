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

    @POST
    public Response addHouse (String json) {

        JSONObject jsonObject = JSONProtocol.getInstance().toJson(json);

        JSONObject object = null;
        if (TokenProtocol.getInstance().isAlive(jsonObject.getString("token"))) {

            object = Database.getInstance().createNewHouse(jsonObject);

        } else {

            object.put("resource", "fail");
            object = new JSONObject().put("result", 0);
        }

        Database.getInstance().commandLog(jsonObject.put("request", "addHouse"));

        return Response.ok(object.toString()).build();
    }

    @POST
    @Path("/room")
    public Response addRoom (String json) {

        JSONObject jsonObject = JSONProtocol.getInstance().toJson(json);

        JSONObject object = null;
        if (TokenProtocol.getInstance().isAlive(jsonObject.getString("token"))) {

            object = Database.getInstance().createNewRoom(jsonObject);

        } else {

            object = new JSONObject().put("result", 0);
        }

        Database.getInstance().commandLog(jsonObject.put("request", "addRoom"));

        return Response.ok(object.toString()).build();
    }

    @POST
    @Path("/device")
    public Response addDevice (String json) {

        JSONObject jsonObject = JSONProtocol.getInstance().toJson(json);

        JSONObject object = null;
        if (TokenProtocol.getInstance().isAlive(jsonObject.getString("token"))) {

            object = Database.getInstance().createNewDevice(jsonObject);

        } else {

            object = new JSONObject().put("result", 0);
        }

        Database.getInstance().commandLog(jsonObject.put("request", "addDevice"));

        return Response.ok(object.toString()).build();
    }

    @PUT
    @Path("/access")
    public Response getAccess (String json) {


        JSONObject jsonObject = JSONProtocol.getInstance().toJson(json);

        JSONObject object = null;
        if (TokenProtocol.getInstance().isAlive(jsonObject.getString("token"))) {

            object = Database.getInstance().addAccessToHouse(jsonObject);

        } else {

            object = new JSONObject().put("result", 0);
        }

        Database.getInstance().commandLog(jsonObject.put("request", "getAccess"));

        return Response.ok(object.toString()).build();
    }

    @DELETE
    @Path("/device")
    public Response deleteDevice (String json) {

        JSONObject jsonObject = JSONProtocol.getInstance().toJson(json);

        JSONObject object = null;
        if (TokenProtocol.getInstance().isAlive(jsonObject.getString("token"))) {

            object = Database.getInstance().deleteDevice(jsonObject);

        } else {

            object.put("resource", "fail");
            object = new JSONObject().put("result", 0);
        }

        Database.getInstance().commandLog(jsonObject);

        return Response.ok(object.toString()).build();
    }
}
