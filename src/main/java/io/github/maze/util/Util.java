package io.github.maze.util;

import io.github.maze.game.GamePanel;
import io.github.maze.maze.GameObject;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.io.InputStream;
import java.util.Random;

public class Util {

    public static final Random rand = new Random(System.nanoTime());

    public static Image getScaledImage(String path) {
        return getScaledImage(path, GamePanel.SCALE);
    }

    public static Image getScaledImage(String path, double scale) {
        InputStream is = Util.class.getResourceAsStream(path);
        if (is == null) {
            throw new IllegalArgumentException("Resource asset not found at path: " + path);
        }
        Image originalImage = new Image(is);

        double targetWidth = originalImage.getWidth() * scale;
        double targetHeight = originalImage.getHeight() * scale;

        ImageView imageView = new ImageView(originalImage);
        imageView.setFitWidth(targetWidth);
        imageView.setFitHeight(targetHeight);

        imageView.setSmooth(false);

        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);

        return imageView.snapshot(params, null);
    }

    public static boolean checkAABB(GameObject a, GameObject b) {
        return a.getX() < b.getX() + b.getWidth() &&
                a.getX() + a.getWidth() > b.getX() &&
                a.getY() < b.getY() + b.getHeight() &&
                a.getY() + a.getHeight() > b.getY();
    }
}
