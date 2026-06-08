package io.github.maze.game;

import javafx.scene.control.Label;

public class UI {

    final GamePanel gp;
    private Label hpLabel;
    private Label flagCountLabel;

    private int lastHP;
    private int lastFlagCount;

    public UI(GamePanel gp) {
        this.gp = gp;

        lastHP = gp.maze.player.getHP();
        hpLabel = new Label("HP: " + lastHP);
        hpLabel.getStyleClass().add("game-font");
        hpLabel.setLayoutX(20);
        hpLabel.setLayoutY(20);

        lastFlagCount = gp.maze.player.flagCount;
        flagCountLabel = new Label("FlagCount: " + lastFlagCount);
        flagCountLabel.getStyleClass().add("game-font");
        flagCountLabel.setLayoutX(20);
        flagCountLabel.setLayoutY(50);

        gp.getChildren().add(hpLabel);
        gp.getChildren().add(flagCountLabel);
    }

    public void render() {
        int currHP = gp.maze.player.getHP();
        if (currHP != lastHP) {
            hpLabel.setText("HP: " + currHP);
            lastHP = currHP;
        }

        int currFlagCount = gp.maze.player.flagCount;
        if (currFlagCount != lastFlagCount) {
            hpLabel.setText("HP: " + currHP);
            lastFlagCount = currFlagCount;
        }
    }
}
