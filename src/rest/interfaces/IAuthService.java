package rest.interfaces;

import rest.models.User;
import rest.models.UserProfileDto;

import javax.naming.AuthenticationException;
import java.util.ArrayList;
import java.util.List;

public interface IAuthService {

    //TODO Shall return object of user
    public User authenticate(String userName, String userPassword) throws AuthenticationException;

    public List<Object> getUsers();

    public String resetSecurityDetails(String userName, String userPassword);

}
