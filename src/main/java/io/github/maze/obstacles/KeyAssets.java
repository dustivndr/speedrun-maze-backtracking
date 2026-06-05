package io.github.maze.obstacles;

import io.github.maze.util.Util;
import javafx.scene.image.Image;

import java.util.Objects;

public class KeyAssets {

    private static final int BASE_WIDTH = 16;
    private static final int BASE_HEIGHT = 16;

    private final Image frame;

    public KeyAssets() {
        frame = Util.getScaledImage("/image/key/key.png");
    }

    public Image getKeyImage() {
        return frame;
    }
}
