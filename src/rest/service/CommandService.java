package rest.service;

import org.json.JSONObject;

import javax.ws.rs.*;

@Path("service/command")
public class CommandService  {

    @GET
    public String ok () {

        return "HEJHEJHEJ";
    }
    @PUT
    public String userRequest (String json) {

        System.out.println(json);
        JSONObject jsonObject = new JSONObject()
                .put("hej", "hej");

        return jsonObject.toString();
    }



}
