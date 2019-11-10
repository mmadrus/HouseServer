package rest.service;

import rest.models.User;
import rest.models.UserProfileDto;

import javax.naming.AuthenticationException;

public interface IAuthService {

    //TODO Shall return object of user
    public User authenticate(String userName, String userPassword) throws AuthenticationException;

    public String resetSecurityDetails(String userName, String userPassword);

}
