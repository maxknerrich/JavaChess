package com.gameofjess.javachess.gui.scenes;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.gameofjess.javachess.gui.controller.Controller;
import com.gameofjess.javachess.gui.controller.MenuController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

/**
 * This class provides an easy-to-use wrapper for the corresponding JavaFX-Scene.
 */
public class JoinScene implements IScene {
    private static final Logger log = LogManager.getLogger(JoinScene.class);
    private static final URL fxmlFileURL = JoinScene.class.getClassLoader().getResource("menu.fxml");
    private final Controller controller;
    private final Scene scene;

    /**
     * Constructs a new HostScene.
     * 
     * @throws IOException as seen in the FXMLLoader's load method.
     */
    JoinScene() throws IOException {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(fxmlFileURL));
        scene = new Scene(loader.load());
        log.debug("Switching scene to {}.", fxmlFileURL.toExternalForm());
        controller = loader.getController();
    }

    /**
     * Gets the JavaFX-Scene object.
     * 
     * @return JavaFX-Scene
     */
    public Scene getFXScene() {
        return scene;
    }

    /**
     * Gets the corresponding controller.
     * 
     * @return corresponding controller.
     */
    public Controller getController() {
        return (MenuController) controller;
    }
}
