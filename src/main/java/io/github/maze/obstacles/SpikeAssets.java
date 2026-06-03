package io.github.maze.obstacles;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

import java.util.Objects;

public class SpikeAssets {

    private static final int FRAME_WIDTH = 16;
    private static final int FRAME_HEIGHT = 16;

    private final Image[] frames = new Image[2];

    public SpikeAssets() {

        Image sheet = new Image(
                Objects.requireNonNull(
                        getClass().getResourceAsStream(
                                "/image/spike/SPIKE.png"
                        )
                )
        );

        frames[0] = new WritableImage(
                sheet.getPixelReader(),
                0, 0,
                FRAME_WIDTH,
                FRAME_HEIGHT
        );

        frames[1] = new WritableImage(
                sheet.getPixelReader(),
                0, FRAME_HEIGHT,
                FRAME_WIDTH,
                FRAME_HEIGHT
        );
    }

    public Image getHiddenFrame() {
        return frames[0];
    }

    public Image getSpikeFrame() {
        return frames[1];
    }
}
