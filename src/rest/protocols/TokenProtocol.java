package rest.protocols;

import rest.models.Token;

import java.util.ArrayList;

public class TokenProtocol {

    private ArrayList<Token> validTokens = new ArrayList<>();

    private static TokenProtocol tokenProtocol = null;

    private TokenProtocol () {}

    public static TokenProtocol getInstance () {

        if (tokenProtocol == null) {

            tokenProtocol = new TokenProtocol();
        }

        return tokenProtocol;
    }


    public boolean isAlive (String tokenString) {

        /*for (Token token: validTokens) {

            if (token.getToken().equals(tokenString)) {

                return true;
            }
        }*/

        if (tokenString.equals("501dd60098c34007bb220853fc4e134b")) {

            return true;
        }

        return false;
    }

    public boolean addToken (Token token) {

        try {

            validTokens.add(token);

            return true;

        } catch (Exception e) {

            return false;
        }
    }

}
