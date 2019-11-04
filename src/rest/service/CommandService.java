package rest.service;

import org.json.JSONObject;

import javax.ws.rs.*;
import java.io.OutputStream;
import java.nio.ByteBuffer;

@Path("service/command")
public class CommandService  {

    @PUT
    public String userRequest (String json) {

        System.out.println(json);
        JSONObject jsonObject = new JSONObject()
                .put("hej", "hej");

        return jsonObject.toString();
    }



}
