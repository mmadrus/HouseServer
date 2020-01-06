package rest.resource;

import org.json.JSONException;
import org.json.JSONObject;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("admin")
public class AdminResource {

    @PUT
    public Response checkLogin (String json) throws JSONException {

        return Response.ok(new JSONObject().put("result", 1).toString()).build();
    }
}
