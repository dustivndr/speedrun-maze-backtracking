package io.github.maze.game;

/*
* Handle MENU UI
*
*/

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class MenuPanel extends VBox {

    public MenuPanel(Game game) {

        setAlignment(Pos.CENTER);
        setSpacing(20);

        Label title = new Label("Alpro MAZE");
        title.getStyleClass().add("title");

        Button startButton = new Button("Best Path");
        startButton.getStyleClass().add("menu-button");

        Button backtrackingButton = new Button("Show Backtracking");
        backtrackingButton.getStyleClass().add("menu-button");

        Button exitButton = new Button("Exit");
        exitButton.getStyleClass().add("menu-button");

        startButton.setOnAction(_ -> game.startGame());

        backtrackingButton.setOnAction(_ -> game.showBacktracking());

        exitButton.setOnAction(_ -> Platform.exit());

        getChildren().addAll(title, startButton, backtrackingButton, exitButton);

        // log debug
        Platform.runLater(() -> {
            title.applyCss();

            System.out.println(title.getFont());
            System.out.println(title.getFont().getFamily());
        });

    }

}
