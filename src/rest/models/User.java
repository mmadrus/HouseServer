package rest.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Entity(name = "Profile")
public class User implements Serializable {
    private static final long serialVersionUID = 7290798953394355234L;

    @Id
    @GeneratedValue
    private String firstName;
    private String lastName;
    private String fullName;
    private String password;
    private String userName;
    private String userId;
    private String email;
    private String salt;
    private String token;
    private String userPassword;


    public User() {
    }

    public User(String firstName, String lastName, String password, String userName, String userId, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.userName = userName;
        this.userId = userId;
        this.email = email;
    }

    public String getFullName() {
        if (fullName == null) {
            fullName = getFirstName() + " " + getLastName();
        }
        return fullName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}
