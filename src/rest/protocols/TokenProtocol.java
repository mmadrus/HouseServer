package rest.protocols;

import rest.models.Token;

import java.util.ArrayList;

public class TokenProtocol {

    private ArrayList<Token> validTokens;

    public TokenProtocol () {

        validTokens = new ArrayList<>();
    }

    public boolean isAlive (String tokenString) {

        for (Token token: validTokens) {

            if (token.getToken().equals(tokenString)) {

                return true;
            }
        }

        return false;
    }

    public boolean createToken () {

        try {

            validTokens.add(new Token());

            return true;

        } catch (Exception e) {

            return false;
        }
    }

}
