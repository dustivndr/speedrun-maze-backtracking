package io.github.maze.particle;

import io.github.maze.game.GamePanel;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

public class MeteorAssets {

    /**
     * 0-2: meteor
     * 3-8: impact frame
     */
    private Image[] frames;

    public static final int BASE_METEOR_WIDTH = 64;
    public static final int BASE_METEOR_HEIGHT = 64;
    public static final int FRAME_METEOR_WIDTH = BASE_METEOR_WIDTH * GamePanel.SCALE;
    public static final int FRAME_METEOR_HEIGHT = BASE_METEOR_HEIGHT * GamePanel.SCALE;

    public static final int BASE_IMPACT_FRAME_WIDTH = 512;
    public static final int BASE_IMPACT_FRAME_HEIGHT = 384;
    public static final int FRAME_IMPACT_FRAME_WIDTH = BASE_IMPACT_FRAME_WIDTH * GamePanel.SCALE;
    public static final int FRAME_IMPACT_FRAME_HEIGHT = BASE_IMPACT_FRAME_HEIGHT * GamePanel.SCALE;

    Image sheet;

    public MeteorAssets() {

        frames = new Image[9];

        sheet = new Image("/image/firemonster/meteor/METEOR.png");

        // meteor
        frames[0] = sliceMeteor(0, 0);
        frames[1] = sliceMeteor(1, 0);
        frames[2] = sliceMeteor(2, 0);

        sheet = new Image("/image/firemonster/meteor/IMPACT FRAME METEOR.png");

        // impact frames
        frames[3] = sliceImpactFrame(0, 0);
        frames[4] = sliceImpactFrame(1, 0);
        frames[5] = sliceImpactFrame(2, 0);
        frames[6] = sliceImpactFrame(0, 1);
        frames[7] = sliceImpactFrame(1, 1);
        frames[8] = sliceImpactFrame(2, 1);

        sheet = null;

    }

    public Image getMeteor(int frame) {

        if (frame < 0 || frame >= meteorSize()) {
            throw new IndexOutOfBoundsException(frame);
        }

        return frames[frame];
    }

    public Image getImpactFrame(int frame) {

        if (frame < 0 || frame >= impactFrameSize()) {
            throw new IndexOutOfBoundsException(frame);
        }

        return frames[frame + meteorSize()];
    }

    public int meteorSize() { return 3; }
    public int impactFrameSize() { return 6; }

    private Image sliceMeteor(int col, int row) {
        return new WritableImage(
                sheet.getPixelReader(),
                col * FRAME_METEOR_WIDTH,
                row * FRAME_METEOR_HEIGHT,
                FRAME_METEOR_WIDTH, FRAME_METEOR_HEIGHT);
    }

    private Image sliceImpactFrame(int col, int row) {
        return new WritableImage(
                sheet.getPixelReader(),
                col * FRAME_IMPACT_FRAME_WIDTH,
                row * FRAME_IMPACT_FRAME_HEIGHT,
                FRAME_IMPACT_FRAME_WIDTH,
                FRAME_IMPACT_FRAME_HEIGHT);
    }
}
