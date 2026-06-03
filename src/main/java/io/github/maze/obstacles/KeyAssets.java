package io.github.maze.obstacles;

import javafx.scene.image.Image;

import java.util.Objects;

public class KeyAssets {

    private static final int FRAME_WIDTH = 16;
    private static final int FRAME_HEIGHT = 16;

    private final Image frame;

    public KeyAssets() {
        frame = new Image("/image/key/key.png");
    }

    public Image getKeyImage() {
        return frame;
    }
}
