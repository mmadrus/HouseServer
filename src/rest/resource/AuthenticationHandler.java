package rest.resource;

import rest.database.Database;
import rest.interfaces.IAuthService;
import rest.models.User;
import rest.models.UserProfileDto;
import rest.utils.AuthUtils;

import javax.naming.AuthenticationException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AuthenticationHandler implements IAuthService {

    Database database;
    AuthUtils authUtils;

    public AuthenticationHandler() {
        this.database = Database.getInstance();
        this.authUtils = new AuthUtils();
    }

    @Override
    public User authenticate(String username, String password) {
        User user = new User();

        database.authenticateUser(username, password);

        if (database.authenticateUser(username, password) == user)

        return user;
    }

   /* @Override
    public User authenticate(String username, String password) throws AuthenticationException {
        UserProfileDto userProfile = new UserProfileDto();

        User userEntity = (User) getUserProfile(username);

        String securePassword = "";

        try {
            securePassword = authUtils.generateSecurePassword(password, userEntity.getSalt());
        } catch (InvalidKeySpecException ix) {
            Logger.getLogger(AuthenticationHandler.class.getName()).log(Level.SEVERE, null, ix);
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

        return userEntity;
    }*/

    @Override
    public List<Object> getUsers() { return database.getAllUsers(); }

    @Override
    public  Object getUserWithName() { return database.findUser("1");}

    @Override
    public String resetSecurityDetails(String userName, String userPassword) {
        return null;
    }


    private Object getUserProfile(String userName) {
        Object returnValue = null;
        try {
             returnValue = this.database.findUser(userName);
        } finally {
            // this.database.closeConnection();
        }
        return returnValue;
    }
}
