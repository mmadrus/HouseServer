package rest;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import rest.service.AuthenticationService;
import rest.resource.ServerTestClass;
import rest.service.CommandService;
import rest.service.LoginService;
import rest.service.StartPageService;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/")
public class Main extends Application {

    @Override
    public Set<Class<?>> getClasses () {
        HashSet h = new HashSet<Class<?>>();
        h.add(StartPageService.class);
        h.add(CommandService.class);
        h.add(LoginService.class);
        h.add(ServerTestClass.class);
        h.add(CreateUserService.class);

        return h;
    }
}

