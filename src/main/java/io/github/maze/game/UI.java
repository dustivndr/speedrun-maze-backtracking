package io.github.maze.game;

import javafx.scene.control.Label;

public class UI {

    final GamePanel gp;
    private Label hpLabel;

    private int lastHP;

    public UI(GamePanel gp) {
        this.gp = gp;

        lastHP = gp.maze.player.getHP();
        hpLabel = new Label("HP: " + lastHP);
        hpLabel.getStyleClass().add("game-font");
        hpLabel.setLayoutX(20);
        hpLabel.setLayoutY(20);

        gp.getChildren().add(hpLabel);
    }

    public void render() {
        int currHP = gp.maze.player.getHP();
        if (currHP != lastHP) {
            hpLabel.setText("HP: " + currHP);
            lastHP = currHP;
        }
    }
}
