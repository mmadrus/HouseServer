package rest.service;

import org.json.JSONObject;
import org.json.JSONStringer;
import rest.protocols.CreateUserProtocol;

import javax.ws.rs.*;
import java.util.UUID;

@Path("service/createUser")
public class CreateUserService  {

    @GET
    public String ok () {

        return "createUser";
    }
    @POST
    public String newUser(String json) {


        CreateUserProtocol newUser1 = new CreateUserProtocol();
        JSONObject obj = new JSONObject();
        obj.put("Username", "Benka33");
        obj.put("email", "something@somethingmore.com");
        obj.put("firstName", "Filip");
        obj.put("lastname", "Bengtsson");
        obj.put("password", "123456");
        System.out.println(obj);
        String user = obj.toString();

        return newUser1.setProtocolString(user);
    }



}
