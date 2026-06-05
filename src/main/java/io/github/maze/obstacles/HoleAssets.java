package io.github.maze.obstacles;

import io.github.maze.game.GamePanel;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

import java.sql.Wrapper;

import io.github.maze.util.Util;

public class HoleAssets {

    private Image[] frames = new Image[3];
    private Image sheet;

    public static int BASE_WIDTH = 16;
    public static int BASE_HEIGHT = 16;

    public static int FRAME_WIDTH = BASE_WIDTH * GamePanel.TILE_SIZE;
    public static int FRAME_HEIGHT = BASE_HEIGHT * GamePanel.TILE_SIZE;

    public HoleAssets() {
        sheet = Util.getScaledImage("/image/hole/HOLE.png");

        frames[0] = sliceImage(0, 0);
        frames[1] = sliceImage(0, 1);
        frames[2] = sliceImage(0, 3);

        sheet = null;
    }

    /**
     *
     * @param phase 0-2
     * @return texture of that animation phase
     */
    public Image getImage(int phase) {
        return frames[phase];
    }

    private Image sliceImage(int col, int row) {
        return new WritableImage(
                sheet.getPixelReader(),
                col * FRAME_WIDTH,
                row * FRAME_HEIGHT,
                FRAME_WIDTH, FRAME_HEIGHT
        );
    }
}
