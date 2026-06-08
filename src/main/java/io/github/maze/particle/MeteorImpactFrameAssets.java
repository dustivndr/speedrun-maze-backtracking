package io.github.maze.particle;

import io.github.maze.game.GamePanel;
import io.github.maze.util.Util;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

public class MeteorImpactFrameAssets {

    private final Image[] frames;

    Image sheet;

    public static final int BASE_WIDTH = 512;
    public static final int BASE_HEIGHT = 384;
    public static final int IMPACT_SCALE = GamePanel.SCALE * 2;
    public static final int FRAME_WIDTH = BASE_WIDTH * IMPACT_SCALE;
    public static final int FRAME_HEIGHT = BASE_HEIGHT * IMPACT_SCALE;

    public static final int BASE_CENTER_X = 243;
    public static final int BASE_CENTER_Y = 206;
    public static final int CENTER_X = BASE_CENTER_X * IMPACT_SCALE;
    public static final int CENTER_Y = BASE_CENTER_Y * IMPACT_SCALE;

    public MeteorImpactFrameAssets() {

        frames = new Image[6];

        sheet = Util.getScaledImage("/image/firemonster/meteor/IMPACT FRAME METEOR.png", IMPACT_SCALE);

        // impact frames
        frames[0] = sliceImage(0, 0);
        frames[1] = sliceImage(1, 0);
        frames[2] = sliceImage(2, 0);
        frames[3] = sliceImage(0, 1);
        frames[4] = sliceImage(1, 1);
        frames[5] = sliceImage(2, 1);

        sheet = null;
    }

    public Image getFrame(int frame) {

        if (frame < 0 || frame >= size()) {
            throw new IndexOutOfBoundsException(frame);
        }

        return frames[frame];
    }

    public int size() {
        return frames.length;
    }

    private Image sliceImage(int col, int row) {
        return new WritableImage(
                sheet.getPixelReader(),
                col * FRAME_WIDTH,
                row * FRAME_HEIGHT,
                FRAME_WIDTH,
                FRAME_HEIGHT);
    }
}
