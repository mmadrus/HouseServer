package rest.resource;

import org.json.JSONArray;
import org.json.JSONObject;
import rest.database.Database;
import rest.protocols.JSONProtocol;
import rest.protocols.TokenProtocol;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@SuppressWarnings("DuplicatedCode")
@Path("service/update")
public class DeviceResource {

    @GET
    @Path("/device")
    public Response getUpdates (String json) {

        JSONObject jsonObject = JSONProtocol.getInstance().toJson(json);
        Database.getInstance().commandLog(jsonObject);
        JSONArray jsonArray = null;
        if (TokenProtocol.getInstance().isAlive(jsonObject.getString("token"))) {

            jsonArray = Database.getInstance().getDeviceUpdate();

        } else {

            jsonArray = new JSONArray().put(new JSONObject().put("result", 0));
        }

        return Response.ok(jsonArray.toString()).build();
    }

    @GET
    @Path("/sensor")
    public Response getSensors (String json) {

        JSONObject jsonObject = JSONProtocol.getInstance().toJson(json);
        Database.getInstance().commandLog(jsonObject);
        JSONObject jsonArray = null;
        if (TokenProtocol.getInstance().isAlive(jsonObject.getString("token"))) {

            jsonArray = Database.getInstance().getSensorUpdate();

        } else {

            jsonArray = new JSONObject().put("result", 0);
        }

        return Response.ok(jsonArray.toString()).build();
    }

    @GET
    @Path("/alarm")
    public Response getAlarms (String json) {

        JSONObject jsonObject = JSONProtocol.getInstance().toJson(json);
        Database.getInstance().commandLog(jsonObject);
        JSONArray jsonArray = null;
        if (TokenProtocol.getInstance().isAlive(jsonObject.getString("token"))) {

            jsonArray = Database.getInstance().getAlarmUpdate();

        } else {

            jsonArray = new JSONArray().put(new JSONObject().put("result", 0));
        }

        return Response.ok(jsonArray.toString()).build();
    }

    @GET
    @Path("/newinstance")
    public Response getCurrentStatus (String json) {

        JSONArray jsonArray = Database.getInstance().getDevices();

        return Response.ok(jsonArray.toString()).build();
    }



}
