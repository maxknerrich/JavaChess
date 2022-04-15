package org.example.javachess.server;

import org.example.javachess.helper.messages.*;
import org.java_websocket.WebSocket;
import org.java_websocket.framing.CloseFrame;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.UUID;

public class Server extends WebSocketServer{

    private static final Logger log = LogManager.getLogger(Server.class);

    private final HashMap<UUID, String> users = new HashMap<>();

    Server (InetSocketAddress address){
        super(address);
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        log.info("New connection from " + webSocket.getRemoteSocketAddress());

        if(users.size() > 1){
            log.warn("Refusing connection because there already are two players!");
            webSocket.close(CloseFrame.UNEXPECTED_CONDITION, "There already are two players!");
            return;
        }

        UUID u = UUID.randomUUID();
        log.debug("Attaching unique ID " + u);
        webSocket.setAttachment(u);

        log.debug("Connection header is being parsed");

        if(clientHandshake.hasFieldValue("username")){
            String username = clientHandshake.getFieldValue("username");
            if(users.containsValue(username)){
                log.info("Username " + username + " already exists! Closing connection.");
                webSocket.send(new ServerMessage(MessageType.SERVERERROR, "Username already in use!").toJSON());
                webSocket.close(CloseFrame.REFUSE, "Username is already used!");
            } else {
                log.info("Username of new user is " + username);
                users.put(u, username);
            }
        } else {
            log.warn("Client with invalid username field tried to connect to the server!");
            webSocket.close(CloseFrame.REFUSE, "Invalid information!");
        }
    }

    @Override
    public void onClose(WebSocket webSocket, int exitCode, String reason, boolean remote) {
        UUID u = webSocket.getAttachment();
        String username = users.get(u);
        log.info("Client " + username + " disconnected!");
        ServerMessage msg = new ServerMessage(MessageType.SERVERINFO, "Client " + username + " disconnected!");
        broadcast(msg.toJSON());
        log.info("Connection from " + webSocket.getRemoteSocketAddress() + " was terminated with exit code " + exitCode + ". Reason: " + reason);
        for(WebSocket ws : getConnections()){
            ws.close(CloseFrame.GOING_AWAY);
        }
    }

    @Override
    public void onMessage(WebSocket webSocket, String message) {
        UUID u = webSocket.getAttachment();
        log.debug("Received message from " + u + " with username " + users.get(u) + ": " + message);
        ClientMessage cmsg = new ClientMessage(message);
        handleClientMessage(cmsg, webSocket);
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {
        log.error("An error occurred in the server instance: " + e.getMessage());
        ServerMessage msg = new ServerMessage(MessageType.SERVERERROR, e.getMessage());
    }

    @Override
    public void onStart() {
        log.info("Server successfully started");
    }

    /**
     * Handles received messages according to their type.
     * @param cmsg ClientMessage to be handled.
     * @param webSocket WebSocket that sent the Message.
     */
    private void handleClientMessage(ClientMessage cmsg, WebSocket webSocket){
        UUID webSocketUUID = webSocket.getAttachment();
        switch (cmsg.getType()) {
            case CHATMESSAGE -> {
                log.info("New chat message received: " + cmsg.getMessage());
                String username = users.get(webSocketUUID);
                ServerMessage msg = new ServerMessage(username, MessageType.CHATMESSAGE, cmsg.getMessage());
                broadcast(msg.toJSON());
            }
            case NEWMOVE -> {
                String username = users.get(webSocketUUID);
                log.info("Client " + username + " has made a new move");

                //TODO: Implement check on move once according method has been implemented.

                ServerMessage msg = new ServerMessage(username, MessageType.NEWMOVE, cmsg.getMessage());
                broadcast(msg.toJSON());
            }
        }
    }

    /**
     * @return String-Array with all current usernames.
     */
    public String[] getUsers(){
        return users.values().toArray(new String[0]);
    }
}
