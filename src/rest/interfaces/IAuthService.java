package rest.interfaces;

import rest.models.User;
import rest.models.UserProfileDto;

import javax.naming.AuthenticationException;
import java.util.ArrayList;
import java.util.List;

public interface IAuthService {

    public String authenticate(String userName, String userPassword) throws AuthenticationException;

    public List<Object> getUsers();

    public Object getUserWithName();

    public String resetSecurityDetails(String userName, String userPassword);

}
