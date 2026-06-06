package io.github.maze.obstacles;

import io.github.maze.game.GamePanel;
import io.github.maze.util.Util;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import jdk.jshell.JShellException;

public class FireAssets {

    Image[] frames = new Image[5];

    Image sheet;

    private static final int BASE_WIDTH = 16;
    private static final int BASE_HEIGHT = 16;
    private static final int FRAME_WIDTH = BASE_WIDTH * GamePanel.SCALE;
    private static final int FRAME_HEIGHT = BASE_HEIGHT * GamePanel.SCALE;

    public FireAssets() {

        sheet = Util.getScaledImage("/image/fire/FIRE.png");

        frames[0] = sliceImage(0);
        frames[1] = sliceImage(1);
        frames[2] = sliceImage(2);
        frames[3] = sliceImage(3);
        frames[4] = sliceImage(4);

        sheet = null;
    }

    private Image sliceImage(int row) {
        return new WritableImage(
                sheet.getPixelReader(),
                0, row * GamePanel.TILE_SIZE,
                FRAME_WIDTH, FRAME_HEIGHT);
    }

    public Image getFrame(int frame) {
        return frames[frame];
    }

    public int size() { return frames.length; }
}
