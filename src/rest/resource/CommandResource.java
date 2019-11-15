package rest.resource;

import org.json.JSONObject;
import rest.protocols.CommandProtocol;
import rest.protocols.JSONProtocol;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("service/command")
public class CommandResource {

    private CommandProtocol commandProtocol = new CommandProtocol();

    @GET
    public String test () {

        JSONObject jo = new JSONObject()
                .put("token", "1234")
                .put("requestType", "command")
                .put("userId", "1234")
                .put("deviceId", 1234)
                .put("command", 1);

        jo = commandProtocol.protocolCheck(jo);

        System.out.println(jo.toString());

        return "Edmir är en bitch";
    }

    @POST
    public Response userRequest (String jsonString) {

       JSONObject jsonObject = JSONProtocol.getInstance().toJson(jsonString);

        System.out.println(jsonObject.toString());

        JSONObject bajs = commandProtocol.protocolCheck(jsonObject);

        return Response.ok(bajs.toString()).header("Access-Control-Allow-Origin", "*").build();
    }



}
