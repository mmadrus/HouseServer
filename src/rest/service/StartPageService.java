package rest.service;


import org.glassfish.tyrus.server.Server;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("")
public class StartPageService {

    @GET
    public Response getStartPage() {

        /*
            .header("Access-Control-Allow-Origin", "*")
      .header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS")
      .header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With")
         */
        return Response.ok("Hello and welcome to the backend. This page does not server any purpose other than removing the 404. Ok bye")
                .build();
    }
}
