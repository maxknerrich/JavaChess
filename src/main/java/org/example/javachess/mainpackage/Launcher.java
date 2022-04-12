package org.example.javachess.mainpackage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.javachess.helper.argumentparsing.ArgumentParser;
import org.example.javachess.helper.argumentparsing.Option;
import org.example.javachess.server.Server;
import org.example.javachess.server.ServerBuilder;
import org.example.javachess.server.ServerCommandListener;

import java.util.Arrays;
import java.util.List;

/**
 * This class is needed to fix the issue with starting JavaFX Applications pointed out here:
 * https://stackoverflow.com/questions/52144931/how-to-add-javafx-runtime-to-eclipse-in-java-11
 *
 * Also, it is used to determine whether the user wants to start a dedicated server or client.
 */
public class Launcher {

    private static final Logger log = LogManager.getLogger(Launcher.class);

    /**
     * Start the application
     */
    public static void main(String[] args){
        try {
            Option[] options = ArgumentParser.getOpts(args);
            List<Option> optionList = Arrays.asList(options);

            if(optionList.contains(Option.dedicatedServer)){
                ServerBuilder sb = new ServerBuilder();
                if(optionList.contains(Option.port)){
                    int port = Integer.parseInt(optionList.get(optionList.indexOf(Option.port)).getValue());
                    sb.setPort(port);
                    log.debug("Manually set server port to " + port);
                }
                if(optionList.contains(Option.host)){
                    String host = optionList.get(optionList.indexOf(Option.host)).getValue();
                    sb.setHost(host);
                    log.debug("Manually set server hostname to " + host);
                }
                Server server = sb.build();
                log.debug("Successfully built server");

                ServerCommandListener commandListener = new ServerCommandListener(server, System.in);
                Thread commandListenerThread = new Thread(commandListener);
                commandListenerThread.start();
                log.debug("Started ServerCommandListener Thread");

                log.info("Starting server on " + server.getAddress().toString());
                server.run();
            } else {
                log.info("Starting client application");
                Main.main(args);
            }
        } catch (Exception e){
            log.error(e.getMessage());
        }




    }
}