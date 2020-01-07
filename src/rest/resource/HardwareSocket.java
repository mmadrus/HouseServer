package rest.resource;

import org.json.JSONObject;
import rest.database.Database;
import rest.protocols.HardwareMessageProtocol;
import rest.utils.Stats;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import static rest.resource.AdminSocket.sendAdmin;

@ServerEndpoint(value="/sockets/arduino")
public class HardwareSocket {

    private Session session;
    private static Set<HardwareSocket> serverEndpoints = new CopyOnWriteArraySet<>();
    private static LinkedList<Session> clients = new LinkedList<>();
    private HardwareMessageProtocol hardwareMessageProtocol = new HardwareMessageProtocol();
    private boolean exists = false;

    @OnOpen
    public void onOpen (Session s) {

        this.session = s;
        serverEndpoints.add(this);
        clients.add(s);
        Stats.getInstance().setHardwareOnline(1);
    }

    @OnMessage
    public void onMessage(Session session, String message)
            throws IOException {

        try {

            String str = message.replace("\r\n", "");
            String result = "";
            String id = "";

            if (str.length() < 6) {
                id = str.substring(0,4);
                result = str.substring(4);

            } else {
                result = (str.substring(0,5));
                id = (str.substring(5));
            }


            Database.getInstance().updateDeviceStatus(new JSONObject().put("deviceID", Integer.parseInt(id)).put("command", Integer.parseInt(result)));
            //sendAdmin(new JSONObject().put("adminCommand", 4).put("id", id).put("result", result).toString());

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @OnClose
    public void onClose(Session session) throws IOException {

        serverEndpoints.remove(this);
        Stats.getInstance().setHardwareOnline(0);
    }

    public static void broadcast(String message)
            throws IOException, EncodeException {

        serverEndpoints.forEach(endpoint -> {
            synchronized (endpoint) {
                try {
                    endpoint.session.getBasicRemote().
                            sendObject(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
