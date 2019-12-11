package rest;

import org.glassfish.tyrus.server.Server;
import rest.resource.*;
import rest.service.HardwareService;
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
        h.add(DeviceResource.class);
        h.add(HardwareSocket.class);
        h.add(StartPageService.class);
        h.add(CommandResource.class);
        h.add(AuthenticationHandler.class);
        h.add(ServerTestClass.class);
        return h;
    }
}
