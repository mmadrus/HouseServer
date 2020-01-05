package rest.resource;

import org.json.JSONObject;
import rest.protocols.HardwareMessageProtocol;
import rest.utils.Stats;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value="/sockets/arduino")
public class HardwareSocket {

    private Session session;
    private static Set<HardwareSocket> serverEndpoints = new CopyOnWriteArraySet<>();
    private static LinkedList<Session> clients = new LinkedList<>();
    private HardwareMessageProtocol hardwareMessageProtocol = new HardwareMessageProtocol();

    @OnOpen
    public void onOpen (Session s) {

        this.session = s;
        serverEndpoints.add(this);
        clients.add(s);
        Stats.getInstance().setHardwareOnline(true);
        Stats.getInstance().sendToAdmin(new JSONObject().put("hardwareOnline", Stats.getInstance().isHardwareOnline()).toString());
    }

    @OnMessage
    public void onMessage(Session session, String message)
            throws IOException {

        try {

            hardwareMessageProtocol.decodeMessage(message);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @OnClose
    public void onClose(Session session) throws IOException {

        serverEndpoints.remove(this);
        Stats.getInstance().setHardwareOnline(false);
        Stats.getInstance().sendToAdmin(new JSONObject().put("hardwareOnline", Stats.getInstance().isHardwareOnline()).toString());
    }

    public static void broadcast(String message)
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
