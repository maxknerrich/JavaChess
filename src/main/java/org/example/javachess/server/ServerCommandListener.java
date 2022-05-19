package org.example.javachess.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.java_websocket.server.WebSocketServer;

public class ServerCommandListener implements Runnable {

    private static final Logger log = LogManager.getLogger(ServerCommandListener.class);

    private Server server;
    private final InputStream stream;

    /**
     * Constructor ServerCommandListener
     * 
     * @param server Server that commands are sent to.
     * @param stream InputStream the commandlistener shall listen on.
     */
    public ServerCommandListener(Server server, InputStream stream) {
        this.server = server;
        this.stream = stream;
    }

    /**
     * Run-method that continually scans for input
     */
    public void run() {
        BufferedReader stdinReader = new BufferedReader(new InputStreamReader(stream));
        String inputLine;

        try {
            while((inputLine = stdinReader.readLine()) != null){
                parseCommand(inputLine);
            }
        } catch(IOException ex){
            log.error(ex.getMessage());
        }
    }

    /**
     * Parses a given command and executes methods accordingly-
     * 
     * @param cmd Command
     */

    public void parseCommand(String cmd) {
        switch (cmd) {
            case "start" -> {
                // To read out whether the server got closed, reflections are needed due to lack of such a method in the websocket API.
                try {
                    Field isClosedField = WebSocketServer.class.getDeclaredField("isclosed");
                    isClosedField.setAccessible(true);

                    if (!((AtomicBoolean) isClosedField.get(server)).get()) {
                        log.error("Server is already started!");
                        break;
                    }

                    int port = server.getPort();
                    String host = server.getAddress().getHostName();

                    ServerBuilder sb = new ServerBuilder();
                    log.debug("Setting server port to {} and host to {}", port, host);
                    sb.setPort(port);
                    sb.setHost(host);

                    server = sb.build();

                    log.info("Starting server...");
                    server.start();
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }

            case "exit" -> {
                log.info("Exiting...");
                System.exit(0);
            }
            case "stop" -> {
                try {
                    log.info("Server is preparing to stop!");

                    boolean isClosed = false;

                    // To read out whether the server got closed, reflections are needed due to lack of a getter-method in the websocket API.
                    Field isClosedField = WebSocketServer.class.getDeclaredField("isclosed");
                    isClosedField.setAccessible(true);

                    long start = System.currentTimeMillis();
                    server.stop(20);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss,SSSS");
                    log.debug("Starting stop procedure at {}.", simpleDateFormat.format(new Date(start)));
                    while (System.currentTimeMillis() - start < 1000) {
                        if (((AtomicBoolean) isClosedField.get(server)).get()) {
                            isClosed = true;
                            break;
                        }
                    }
                    long end = System.currentTimeMillis();

                    if(isClosed){
                        log.info("Server successfully closed within {} ms!", end-start);
                        log.debug("Exact closing time: " + simpleDateFormat.format(new Date(end)));
                        log.info("Continue with 'exit' to exit or with 'start' to start again");
                    } else {
                        throw new RuntimeException("Could not stop server in time!");
                    }

                } catch (InterruptedException e) {
                    log.error("Interrupted with message: {}", e.getMessage());
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            case "list" -> {
                String users = "";
                Stream<String> serverUserStream = Stream.of(server.getUsers()).sorted();
                String[] serverUsers = serverUserStream.toArray(String[]::new);
                for (int i = 0; i < serverUsers.length; i++) {
                    String s = serverUsers[i];
                    if(i == serverUsers.length - 1){
                        users = users.concat(s);
                    } else {
                        users = users.concat(s + ", ");
                    }
                }
                log.info("The following users are connected: " + users);
            }
            default -> log.info("Unknown command: " + cmd);
        }
    }
}
