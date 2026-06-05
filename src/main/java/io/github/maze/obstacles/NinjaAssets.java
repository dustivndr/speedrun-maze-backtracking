package io.github.maze.obstacles;

import io.github.maze.game.GamePanel;
import io.github.maze.util.Util;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

import java.util.ArrayList;
import java.util.List;

public class NinjaAssets {

    private List<Image> frames = new ArrayList<>();
    Image sheet;

    public static final int BASE_WIDTH = 16;
    public static final int BASE_HEIGHT = 16;
    public static final int FRAME_WIDTH = BASE_WIDTH * GamePanel.SCALE;
    public static final int FRAME_HEIGHT = BASE_HEIGHT * GamePanel.SCALE;

    public NinjaAssets() {
        sheet = Util.getScaledImage("/image/ninja/NINJA NPC.png");

        frames.add(sliceImage(0, 0));
        frames.add(sliceImage(0, 4));
        frames.add(sliceImage(0, 8));
        frames.add(Util.getScaledImage("/image/ninja/ninja back.png"));

        sheet = null;
    }

    public Image getFrame(String key) {
        return switch (key) {
            case "down" -> frames.get(0);
            case "left" -> frames.get(1);
            case "right" -> frames.get(2);
            case "up" -> frames.get(3);
            default -> null;
        };
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
