package rest.resource;

import org.json.JSONObject;
import rest.database.Database;
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
        Stats.getInstance().setAdminOnline(true);
    }

    @OnMessage
    public void onMessage(String message)
            throws IOException {

        try {

            int adminCommand = Integer.parseInt(message);
            System.out.println("HAAAAAAALLÅÅÅÅÅÅÅ: " + adminCommand);

            switch (adminCommand) {

                case 1:

                    sendAdmin(new JSONObject().put("hardwareOnline", Stats.getInstance().isHardwareOnline()).put("adminCommand", 1).toString());
                    break;

                case 3:

                    sendAdmin(Database.getInstance().getDBCount().put("adminCommand", 3).toString());
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
        Stats.getInstance().setAdminOnline(false);
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
