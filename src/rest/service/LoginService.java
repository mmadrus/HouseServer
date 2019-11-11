package rest.service;

//import rest.protocols.LoginProtocol;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("service/login")
public class LoginService {

   // private LoginProtocol loginProtocol = new LoginProtocol();
    private String protocolType, responseString;
    private Response response;

    private IAuthService authService;

    @GET
    @Path("ok")
    public String ok () {

        return "ok";
    }


    @POST
    public String protocolCheck (String protocolString) {

        protocolType = protocolString.substring(0,1);

        if (protocolString.equals("C")) {

            //responseString = loginProtocol.setProtocolString(protocolString);



        }
        return "ok";
    }
}
