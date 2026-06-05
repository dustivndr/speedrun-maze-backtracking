package io.github.maze.entities;

import io.github.maze.game.GamePanel;
import io.github.maze.util.Util;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

import java.util.ArrayList;
import java.util.List;

public class PlayerAssets {

    int BASE_WIDTH = 16;
    int BASE_HEIGHT = 16;

    int FRAME_WIDTH = BASE_WIDTH * GamePanel.SCALE;
    int FRAME_HEIGHT = BASE_HEIGHT * GamePanel.SCALE;

    private final List<Image> frames = new ArrayList<>();
    private Image sheet;

    public PlayerAssets() {
        sheet = Util.getScaledImage("/image/player/player.png");

        frames.add(sliceImage(0, 0));
        frames.add(sliceImage(0, 1));
        frames.add(sliceImage(0, 3));

        frames.add(sliceImage(0, 4));
        frames.add(sliceImage(0, 5));
        frames.add(sliceImage(0, 7));

        frames.add(sliceImage(0, 8));
        frames.add(sliceImage(0, 9));
        frames.add(sliceImage(0, 11));

        frames.add(sliceImage(0, 13));
        frames.add(sliceImage(0, 14));
        frames.add(sliceImage(0, 16));

        sheet = null;
    }

    public Image getTexture(String key) {
        switch (key) {
            case "upStationary":    return frames.get(9);
            case "upWalk1":         return frames.get(10);
            case "upWalk2":         return frames.get(9);
            case "upWalk3":         return frames.get(11);
            case "upWalk4":         return frames.get(9);

            case "downStationary":  return frames.get(0);
            case "downWalk1":       return frames.get(1);
            case "downWalk2":       return frames.get(0);
            case "downWalk3":       return frames.get(2);
            case "downWalk4":       return frames.get(0);

            case "leftStationary":  return frames.get(3);
            case "leftWalk1":       return frames.get(4);
            case "leftWalk2":       return frames.get(3);
            case "leftWalk3":       return frames.get(5);
            case "leftWalk4":       return frames.get(3);

            case "rightStationary": return frames.get(6);
            case "rightWalk1":      return frames.get(7);
            case "rightWalk2":      return frames.get(6);
            case "rightWalk3":      return frames.get(8);
            case "rightWalk4":      return frames.get(6);

            default: return null;
        }
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
