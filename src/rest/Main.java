package rest;

import rest.service.CommandService;
import rest.service.CreateUserService;
import rest.service.HardwareService;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/")
public class Main extends Application {

    @Override
    public Set<Class<?>> getClasses () {

        HashSet h = new HashSet<Class<?>>();
        h.add(CommandService.class);
        h.add(CreateUserService.class);
        return h;
    }
}
