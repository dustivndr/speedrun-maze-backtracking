package io.github.maze.util;

import io.github.maze.game.GamePanel;
import javafx.scene.image.Image;

import java.util.Random;

public class Util {

    public static final Random rand = new Random(System.nanoTime());

    public static Image getScaledImage(String path) {
        Image ogSheet = new Image(path);

        double scaledWidth = ogSheet.getWidth() * GamePanel.SCALE;
        double scaledHeight = ogSheet.getHeight() * GamePanel.SCALE;

        return new Image(
                path,
                scaledWidth,
                scaledHeight,
                true,
                false
        );
    }
}
