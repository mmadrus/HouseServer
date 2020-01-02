package rest.resource;

import rest.protocols.HardwareMessageProtocol;

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
    }

    @OnMessage
    public void onMessage(Session session, String message)
            throws IOException {

        try {

            System.out.println(message);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @OnClose
    public void onClose(Session session) throws IOException {

        serverEndpoints.remove(this);
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
