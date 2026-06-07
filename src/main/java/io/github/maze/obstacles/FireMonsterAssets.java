package io.github.maze.obstacles;

import io.github.maze.game.GamePanel;
import io.github.maze.util.Util;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

public class FireMonsterAssets {

    Image[] frames = new Image[6];

    Image sheet;

    public final int BASE_WIDTH = 32;
    public final int BASE_HEIGHT = 32;
    public final int FRAME_WIDTH = BASE_WIDTH * GamePanel.SCALE;
    public final int FRAME_HEIGHT= BASE_HEIGHT * GamePanel.SCALE;

    public FireMonsterAssets() {

        sheet = Util.getScaledImage("/image/firemonster/FIRE MONSTER.png", GamePanel.SCALE / 2.0);

        frames[0] = sliceImage(0, 0);
        frames[1] = sliceImage(1, 0);
        frames[2] = sliceImage(2, 0);
        frames[3] = sliceImage(0, 1);
        frames[4] = sliceImage(1, 1);
        frames[5] = sliceImage(2, 1);

        sheet = null;

    }

    public Image getIdle(int frame) {
        if (frame < 0 || frame >= idleSize()) {
            throw new IndexOutOfBoundsException(frame);
        }

        return frames[frame];
    }

    public Image getAttack(int frame) {
        if (frame < 0 || frame >= attackSize()) {
            throw new IndexOutOfBoundsException(frame);
        }

        return frames[frame + idleSize()];
    }

    public int idleSize() { return 2; }
    public int attackSize() { return 4; }

    private Image sliceImage(int col, int row) {
        return new WritableImage(
                sheet.getPixelReader(),
                col * FRAME_WIDTH,
                row * FRAME_HEIGHT,
                FRAME_WIDTH, FRAME_HEIGHT);
    }
}
