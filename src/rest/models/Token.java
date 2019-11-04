package rest.models;

import java.util.UUID;

public class Token {

    private String token;

    public Token() {
        String uuid = UUID.randomUUID().toString();
        uuid = uuid.replace("-", "");
        this.token = uuid;
    }

    public String getToken() {
        return token;
    }

}
