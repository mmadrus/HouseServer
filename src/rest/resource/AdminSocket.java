package rest.resource;

import org.json.JSONObject;
import rest.utils.Stats;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value="/sockets/admin")
public class AdminSocket {

    private Session session;
    private static Set<AdminSocket> serverEndpoints = new CopyOnWriteArraySet<>();
    private static LinkedList<Session> clients = new LinkedList<>();

    @OnOpen
    public void onOpen (Session s) {

        this.session = s;
        serverEndpoints.add(this);
        clients.add(s);
    }

    @OnMessage
    public void onMessage(Session session, String message)
            throws IOException {

        try {

            int adminCommand = Integer.parseInt(message);

            switch (adminCommand) {

                case 1:

                    //sendAdmin(new JSONObject().put("hardwareOnline", Stats.getInstance().isHardwareOnline()).toString());
                    break;

                case 2:

                    //sendAdmin(new JSONObject().put("serverOnline", true).toString());
                    break;

                default:

                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @OnClose
    public void onClose(Session session) throws IOException {

        serverEndpoints.remove(this);
    }

    public static void sendAdmin(String message)
            throws IOException, EncodeException {

        serverEndpoints.forEach(endpoint -> {
            synchronized (endpoint) {
                try {
                    endpoint.session.getBasicRemote().
                            sendObject(message);
                } catch (IOException | EncodeException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
