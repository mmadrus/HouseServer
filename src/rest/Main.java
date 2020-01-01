package rest;

import org.glassfish.grizzly.http.server.HttpServer;
import rest.resource.*;
import rest.resource.LoginResource;
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
        h.add(HardwareSocket.class);
        h.add(StartPageService.class);
        h.add(CommandResource.class);
        h.add(LoginResource.class);
        h.add(ServerTestClass.class);
        h.add(CreateUserResource.class);
        h.add(DeviceResource.class);
        h.add(HouseResource.class);

        return h;
    }
}

