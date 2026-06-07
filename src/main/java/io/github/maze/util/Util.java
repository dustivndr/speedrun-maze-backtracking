package io.github.maze.util;

import io.github.maze.game.GamePanel;
import io.github.maze.maze.GameObject;
import javafx.scene.image.Image;

import java.util.Random;

public class Util {

    public static final Random rand = new Random(System.nanoTime());

    public static Image getScaledImage(String path) {
        return getScaledImage(path, GamePanel.SCALE);
    }

    public static Image getScaledImage(String path, double scale) {
        Image ogSheet = new Image(path);

        double scaledWidth = ogSheet.getWidth() * scale;
        double scaledHeight = ogSheet.getHeight() * scale;

        return new Image(
                path,
                scaledWidth,
                scaledHeight,
                true,
                false
        );
    }

    public static boolean checkAABB(GameObject a, GameObject b) {
        return a.getX() < b.getX() + b.getWidth() &&
                a.getX() + a.getWidth() > b.getX() &&
                a.getY() < b.getY() + b.getHeight() &&
                a.getY() + a.getHeight() > b.getY();
    }
}
