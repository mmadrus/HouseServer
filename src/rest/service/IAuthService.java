package rest.service;

import rest.models.UserProfileDto;

import javax.naming.AuthenticationException;

public interface IAuthService {

    //TODO Shall return object of user
    public UserProfileDto authenticate(String userName, String userPassword) throws AuthenticationException;

    public String resetSecurityDetails(String userName, String userPassword);

}
