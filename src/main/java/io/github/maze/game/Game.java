package io.github.maze.game;

/*
* CONTROL STATE
* decide : Game / Menu
*
*/

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
    }

}
