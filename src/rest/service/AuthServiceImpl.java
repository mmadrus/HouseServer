package rest.service;

import rest.utils.AuthUtils;

import javax.naming.AuthenticationException;

public class AuthServiceImpl implements IAuthService {

    String database;
    AuthUtils authUtils;

    public AuthServiceImpl(String database, AuthUtils authUtils) {
        this.database = database;
        this.authUtils = authUtils;
    }

    @Override
    public String authenticate(String username, String password) throws AuthenticationException {
        return null;
    }

    @Override
    public String resetSecurityDetails(String userName, String userPassword) {
        return null;
    }
}