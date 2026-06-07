package io.github.maze.particle;

import io.github.maze.util.Util;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

import java.util.List;

public class ThunderAssets {

    Image[] frames = new Image[6];
    Image sheet;

    static final int BASE_WIDTH = 32;
    static final int BASE_HEIGHT = 128;
    static final int SCALE = 5;
    static final int FRAME_WIDTH = BASE_WIDTH * SCALE;
    static final int FRAME_HEIGHT = BASE_HEIGHT * SCALE;

    public ThunderAssets() {

        sheet = Util.getScaledImage("/image/wizard/thunder/THUNDER.png", SCALE);

        frames[0] = sliceImage(0, 0);
        frames[1] = sliceImage(1, 0);
        frames[2] = sliceImage(2, 0);
        frames[3] = sliceImage(3, 0);
        frames[4] = sliceImage(4, 0);
        frames[5] = sliceImage(5, 0);

        sheet = null;

    }

    public Image getFrame(int frame) {
        return frames[frame];
    }

    private Image sliceImage(int col, int row) {
        return new WritableImage(
                sheet.getPixelReader(),
                col * FRAME_WIDTH,
                row * FRAME_HEIGHT,
                FRAME_WIDTH, FRAME_HEIGHT);
    }
}
