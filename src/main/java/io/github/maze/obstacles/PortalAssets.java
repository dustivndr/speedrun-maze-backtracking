package io.github.maze.obstacles;

import io.github.maze.game.GamePanel;
import io.github.maze.util.Util;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

public class PortalAssets {

    private Image[] frames = new Image[20];

    Image sheet;

    private static final int BASE_WIDTH = 32;
    private static final int BASE_HEIGHT = 32;
    private static final int FRAME_WIDTH = BASE_WIDTH * GamePanel.SCALE;
    private static final int FRAME_HEIGHT = BASE_HEIGHT * GamePanel.SCALE;

    public PortalAssets() {

        sheet = Util.getScaledImage("/image/portal/PORTAL.png");

        frames[0] = sliceImage(0, 0);
        frames[1] = sliceImage(1, 0);
        frames[2] = sliceImage(2, 0);
        frames[3] = sliceImage(3, 0);
        frames[4] = sliceImage(4, 0);

        frames[5] = sliceImage(0, 1);
        frames[6] = sliceImage(1, 1);
        frames[7] = sliceImage(2, 1);
        frames[8] = sliceImage(3, 1);
        frames[9] = sliceImage(4, 1);

        frames[10] = sliceImage(0, 2);
        frames[11] = sliceImage(1, 2);
        frames[12] = sliceImage(2, 2);
        frames[13] = sliceImage(3, 2);
        frames[14] = sliceImage(4, 2);

        frames[15] = sliceImage(0, 3);
        frames[16] = sliceImage(1, 3);
        frames[17] = sliceImage(2, 3);
        frames[18] = sliceImage(3, 3);
        frames[19] = sliceImage(4, 3);

    }

    public Image getFrame(int frame) {

        if (frame < 0 || frame >= size()) {
            throw new IndexOutOfBoundsException(frame);
        }

        return frames[frame];
    }

    public int size() { return frames.length; }

    public Image sliceImage(int col, int row) {
        return new WritableImage(
                sheet.getPixelReader(),
                col * FRAME_WIDTH,
                row * FRAME_HEIGHT,
                FRAME_WIDTH, FRAME_HEIGHT);
    }
}
