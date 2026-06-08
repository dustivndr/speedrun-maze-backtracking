package io.github.maze.entities;

import io.github.maze.game.GamePanel;
import io.github.maze.util.Util;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

public class MeteorAssets {

    private Image[] frames;

    public static final int BASE_METEOR_WIDTH = 64;
    public static final int BASE_METEOR_HEIGHT = 64;
    public static final int FRAME_METEOR_WIDTH = BASE_METEOR_WIDTH * GamePanel.SCALE;
    public static final int FRAME_METEOR_HEIGHT = BASE_METEOR_HEIGHT * GamePanel.SCALE;

    public static final int BASE_METEOR_CENTER_X = 19;
    public static final int BASE_METEOR_CENTER_Y = 46;
    public static final int FRAME_METEOR_CENTER_X = BASE_METEOR_CENTER_X * GamePanel.SCALE;
    public static final int FRAME_METEOR_CENTER_Y = BASE_METEOR_CENTER_Y * GamePanel.SCALE;

    Image sheet;

    public MeteorAssets() {

        frames = new Image[3];

        sheet = Util.getScaledImage("/image/firemonster/meteor/METEOR.png");

        frames[0] = sliceImage(0, 0);
        frames[1] = sliceImage(1, 0);
        frames[2] = sliceImage(2, 0);

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
                col * FRAME_METEOR_WIDTH,
                row * FRAME_METEOR_HEIGHT,
                FRAME_METEOR_WIDTH, FRAME_METEOR_HEIGHT);
    }
}
