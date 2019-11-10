package rest.service;

import org.json.JSONObject;
import rest.protocols.CommandProtocol;
import rest.protocols.JSONProtocol;

import javax.ws.rs.*;

@Path("service/command")
public class CommandService  {

    private JSONProtocol jsonProtocol = new JSONProtocol();
    private CommandProtocol commandProtocol = new CommandProtocol();

    @PUT
    public String userRequest (String jsonString) {

        JSONObject jsonObject = jsonProtocol.toJson(jsonString);

        jsonObject = commandProtocol.protocolCheck(jsonObject);

        return jsonProtocol.fromJson(jsonObject);
    }



}
