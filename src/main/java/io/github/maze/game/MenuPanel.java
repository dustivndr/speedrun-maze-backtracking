package io.github.maze.game;

/*
* Handle MENU UI
*
*/

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class MenuPanel extends VBox {

    public MenuPanel(Game game) {

        Label title = new Label("Alpro MAZE");
        Button startButton = new Button("Start");
        Button exitButton = new Button("Exit");

        startButton.setOnAction(e -> {
            game.startGame();
        });

        exitButton.setOnAction(e -> {
            Platform.exit();
        });

        getChildren().addAll(title, startButton, exitButton);

    }

}
