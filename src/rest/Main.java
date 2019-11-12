package rest;

import rest.resource.AuthenticationEndpoint;
import rest.resource.ServerTestClass;
import rest.service.CommandService;
import rest.service.LoginService;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/")
public class Main extends Application {

    @Override
    public Set<Class<?>> getClasses () {

        HashSet h = new HashSet<Class<?>>();
        h.add(LoginService.class);
        h.add(CommandService.class);
        h.add(AuthenticationEndpoint.class);
        h.add(ServerTestClass.class);
        return h;
    }
}
