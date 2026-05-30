package io.github.maze.game;

/*
* CONTROL STATE
* decide : Game / Menu
*
*/

import javafx.scene.layout.Pane;

public class Game extends Pane {

    private GameState currentState;

    private final MenuPanel menuPanel;
    private final GamePanel gamePanel;

    public Game() {

        menuPanel = new MenuPanel(this);
        gamePanel = new GamePanel(this);

        setState(GameState.MENU);
    }

    public void setState(GameState state) {

        currentState = state;

        getChildren().clear();

        switch (state) {
            case MENU -> getChildren().add(menuPanel);
            case GAME -> getChildren().add(gamePanel);
        }
    }

    public void start() {
        // game loop later
    }

}
