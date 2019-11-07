package rest.service;

import rest.database.Database;
import rest.models.UserProfileDto;
import rest.models.UserProfileEntity;
import rest.utils.AuthUtils;

import javax.naming.AuthenticationException;
import java.security.spec.InvalidKeySpecException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AuthServiceImpl implements IAuthService {

    //should be db
    Database database;
    AuthUtils authUtils;

    public AuthServiceImpl(Database database, AuthUtils authUtils) {
        this.database = database;
        this.authUtils = authUtils;


    }


    @Override
    public UserProfileDto authenticate(String username, String password) throws AuthenticationException {
        UserProfileDto userProfile = new UserProfileDto();

        UserProfileEntity userEntity = getUserProfile(username);

        String securePassword = "";

        try {
            securePassword = authUtils.generateSecurePassword(password, userEntity.getSalt());
        } catch (InvalidKeySpecException ix) {
            Logger.getLogger(AuthServiceImpl.class.getName()).log(Level.SEVERE, null, ix);
            throw new AuthenticationException(ix.getLocalizedMessage());
        }

        boolean authenticated = false;

        if (securePassword != null && securePassword.equalsIgnoreCase(userEntity.getUserPassword())) {
            if(username != null && username.equalsIgnoreCase(userEntity.getUserName())) {
                authenticated = true;
            }
        }

        if (!authenticated) {
            throw new AuthenticationException("Authentication failed!");
        }


        return userProfile;
    }

    @Override
    public String resetSecurityDetails(String userName, String userPassword) {
        return null;
    }


    private Object getUserProfile(String userName) {
        Object returnValue = null;
        try {
            //connect to database and get userprofile
             this.database.openConnection();
             returnValue = this.database.findUser(userName);
        } finally {
            // this.database.closeConnection();
        }
        return returnValue;
    }
}
