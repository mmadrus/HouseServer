package rest;

import rest.resource.CommandResource;
import rest.resource.DeviceResource;
import rest.resource.HardwareSocket;
import rest.resource.ServerTestClass;
import rest.service.CreateUserService;
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
        h.add(HardwareSocket.class);
        h.add(StartPageService.class);
        h.add(CommandResource.class);
        h.add(LoginService.class);
        h.add(ServerTestClass.class);
        h.add(CreateUserService.class);
        h.add(DeviceResource.class);

        return h;
    }
}

