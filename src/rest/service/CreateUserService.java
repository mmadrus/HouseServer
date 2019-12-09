package rest.service;

import org.json.JSONException;
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
    @PUT
    public String newUser(String json) throws JSONException {

        /*System.out.println(json);
        JSONObject jsonObject = new JSONObject()
                .put("hej", "hej");

        return jsonObject.toString();*/

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
