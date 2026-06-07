package io.github.maze.obstacles;

import io.github.maze.util.Util;
import javafx.scene.image.Image;

public class FlagGreenAssets {

    private final Image frame;

    public FlagGreenAssets() {
        frame = Util.getScaledImage("/image/flag/flag.png");
    }

    public Image getFlagGreenImage() {
        return frame;
    }

}
