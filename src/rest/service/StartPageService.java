package rest.service;


import org.glassfish.tyrus.server.Server;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("")
public class StartPageService {

    @GET
    public String getStartPage() {

        return "Hello and welcome to the backend. This page does not server any purpose other than removing the 404. Ok bye";
    }
}
