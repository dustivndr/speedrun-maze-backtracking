package io.github.maze.obstacles;

import io.github.maze.game.GamePanel;
import io.github.maze.util.Util;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

public class WizardAssets {

    private Image[] frames = new Image[11];

    private Image sheet;

    public static final int BASE_WIDTH = 16;
    public static final int BASE_HEIGHT = 16;
    public static final int FRAME_WIDTH = BASE_WIDTH * GamePanel.SCALE;
    public static final int FRAME_HEIGHT = BASE_HEIGHT * GamePanel.SCALE;

    public WizardAssets() {
        sheet = Util.getScaledImage("/image/wizard/wizard.png");

        frames[0] = sliceImage(2, 0);
        frames[1] = sliceImage(2, 2);
        frames[2] = sliceImage(3, 2);
        frames[3] = sliceImage(0, 4);

        frames[4] = sliceImage(3, 0);
        frames[5] = sliceImage(0, 1);
        frames[6] = sliceImage(1, 1);
        frames[7] = sliceImage(2, 1);
        frames[8] = sliceImage(3, 1);
        frames[9] = frames[7];
        frames[10] = frames[8];
    }

    public Image getDirection(String dir) {
        return switch (dir) {
            case "down" -> frames[0];
            case "left" -> frames[1];
            case "right" -> frames[2];
            case "up" -> frames[3];
            default -> null;
        };
    }

    public Image getAttackAnimation(int frame) {
        return frames[frame + 4];
    }

    public int attackSize() { return 7; }

    private Image sliceImage(int col, int row) {
        return new WritableImage(
                sheet.getPixelReader(),
                col * FRAME_WIDTH,
                row * FRAME_HEIGHT,
                FRAME_WIDTH, FRAME_HEIGHT
        );
    }
}
