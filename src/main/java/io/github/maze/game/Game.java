package io.github.maze.game;

/*
* CONTROL STATE
* decide : Game / Menu
*
*/

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;

public class Game extends Pane {

    private final MenuPanel menuPanel;
    private final GamePanel gamePanel;

    public Game() {
        
        menuPanel = new MenuPanel(this);
        gamePanel = new GamePanel(this);

        showMenu();
    }

    public void showMenu() {
        menuPanel.prefWidthProperty().bind(widthProperty());
        menuPanel.prefHeightProperty().bind(heightProperty());

        getChildren().setAll(menuPanel);
    }

    public void startGame() {
        getChildren().setAll(gamePanel);
        gamePanel.startGameThread("bestPath");
    }

    public void showBacktracking() {
        getChildren().setAll(gamePanel);
        gamePanel.startGameThread("showBacktracking");
    }

    public void finishGame() {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Maze");
        alert.setHeaderText("Maze Completed!");
        alert.setContentText("What would you like to do?");

        ButtonType menu = new ButtonType("Menu");
        ButtonType exit = new ButtonType("Exit");

        alert.getButtonTypes().setAll(menu, exit);

        alert.showAndWait().ifPresent(button -> {

            if (button == menu) {
                showMenu();
            } else {
                Platform.exit();
            }

        });
    }

}
