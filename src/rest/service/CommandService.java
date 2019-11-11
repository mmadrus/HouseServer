package rest.service;

import org.json.JSONObject;
import rest.protocols.CommandProtocol;
import rest.protocols.JSONProtocol;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("service/command")
public class CommandService  {

    private JSONProtocol jsonProtocol = new JSONProtocol();
    private CommandProtocol commandProtocol = new CommandProtocol();

    @GET
    public String test () {

        JSONObject jo = new JSONObject()
                .put("token", "1234")
                .put("request-type", "command")
                .put("user-id", "1234")
                .put("device-id", "1234")
                .put("command", 2);

        jo = commandProtocol.protocolCheck(jo);

        System.out.println(jo.toString());

        return "ok";
    }

    @PUT
    public Response userRequest (String jsonString) {

       JSONObject jsonObject = jsonProtocol.toJson(jsonString);

        System.out.println(jsonObject.toString());

        JSONObject bajs = commandProtocol.protocolCheck(jsonObject);

        return Response.ok(bajs.toString()).build();
    }



}
