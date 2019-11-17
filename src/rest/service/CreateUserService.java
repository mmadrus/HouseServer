package rest.service;

import com.google.gson.JsonObject;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.json.JSONTokener;
import rest.protocols.CreateUserProtocol;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.UUID;

@Path("service/createUser")
public class CreateUserService  {

    private CreateUserProtocol userProtocol = new CreateUserProtocol();

    @GET
    public String ok () {

        return "createUser";
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public String newUser(String json) {

        /*System.out.println(json);
        JSONObject jsonObject = new JSONObject()
                .put("hej", "hej");
        return jsonObject.toString();*/

        // newUser is the JSON that has the information from the client and should be sent to the
        JSONObject newUser = new JSONObject(new JSONTokener(json));

            /*JSONObject test = new JSONObject();
            test.put("userName", "Benka33");
            test.put("email", "something@somethingmore.com");
            test.put("firstName", "Filip");
            test.put("lastName", "Bengtsson");
            test.put("password", "123456");
            //System.out.println(obj);*/
            //String user = obj.toString();

            return userProtocol.setProtocolString(newUser).toString();
    }



}
