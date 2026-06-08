package io.github.maze.game;

import io.github.maze.util.Util;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class UI {

    final GamePanel gp;
    private Label hpLabel;
    private Label flagCountLabel;

    private int lastHP;
    private int lastFlagCount;

    public UI(GamePanel gp) {
        this.gp = gp;

        lastHP = gp.maze.player.getHP();
        hpLabel = new Label("HP: " + lastHP, new ImageView(Util.getScaledImage("/image/ui/heart.png", GamePanel.SCALE * 1.5)));
        hpLabel.setGraphicTextGap(10.0);
        hpLabel.getStyleClass().add("game-font");
        hpLabel.setLayoutX(20);
        hpLabel.setLayoutY(20);

        lastFlagCount = 0;
        flagCountLabel = new Label(
                lastFlagCount + " / " + gp.maze.flagCount,
                new ImageView(Util.getScaledImage("/image/flag/flag.png")));
        flagCountLabel.setGraphicTextGap(7.0);
        flagCountLabel.getStyleClass().add("game-font");
        flagCountLabel.setLayoutX(18);
        flagCountLabel.setLayoutY(50);

        gp.getChildren().add(hpLabel);
        gp.getChildren().add(flagCountLabel);
    }

    public void render() {

        // hp
        int currHP = gp.maze.player.getHP();
        if (currHP != lastHP) {
            hpLabel.setText("" + currHP);
            lastHP = currHP;
        }

        // flag
        int currFlagCount = gp.maze.player.flagCount;
        if (currFlagCount != lastFlagCount) {
            flagCountLabel.setText(currFlagCount + " / " + gp.maze.flagCount);
            lastFlagCount = currFlagCount;
        }
    }
}
