package rest.resource;

import rest.database.Database;
import rest.service.AuthServiceImpl;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.xml.ws.Response;

@Path("/getuser")
public class ServerTestClass {

    AuthServiceImpl authService;
    Database database = Database.getInstance();

    @GET
    public Response getUsers() {
        Object result = authService.getUsers();

    }
}
