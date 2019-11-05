package rest.service;

import javax.naming.AuthenticationException;

public interface IAuthService {

    //TODO Shall return object of user
    public String authenticate(String userName, String userPassword) throws AuthenticationException;

    public String resetSecurityDetails(String userName, String userPassword);

}
