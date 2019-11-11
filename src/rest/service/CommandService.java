package rest.service;

import org.json.JSONObject;
import rest.protocols.CommandProtocol;
import rest.protocols.JSONProtocol;

import javax.ws.rs.*;

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

        return jo.getString("result");
    }

    @PUT
    public String userRequest (String jsonString) {

        JSONObject jsonObject = jsonProtocol.toJson(jsonString);

        jsonObject = commandProtocol.protocolCheck(jsonObject);

        return jsonProtocol.fromJson(jsonObject);
    }



}
