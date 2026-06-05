package io.github.maze.obstacles;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

import java.io.WriteAbortedException;
import java.util.ArrayList;
import java.util.List;

public class BushWallAssets {

    private List<Image> textures = new ArrayList<>();

    Image sheet;

    public final int FRAME_WIDTH = 16;
    public final int FRAME_HEIGHT = 32;

    public BushWallAssets() {
        sheet = new Image("/image/bush/bush walllll.png");

        textures.add(sliceImage(0, 0)); // 0  : left-right ver. 1
        textures.add(sliceImage(1, 0)); // 1  : left-right ver. 2
        textures.add(sliceImage(2, 0)); // 2  : right
        textures.add(sliceImage(3, 0)); // 3  : up-right
        textures.add(sliceImage(4, 0)); // 4  : up-left
        textures.add(sliceImage(5, 0)); // 5  : right-bottom
        textures.add(sliceImage(0, 1)); // 6  : left
        textures.add(sliceImage(1, 1)); // 7  : left-down
        textures.add(sliceImage(2, 1)); // 8  : single
        textures.add(sliceImage(3, 1)); // 9  : up-down ver. 1
        textures.add(sliceImage(4, 1)); // 10 : up-down ver. 2
        textures.add(sliceImage(5, 1)); // 11 : up
        textures.add(sliceImage(0, 2)); // 12 : down
        textures.add(sliceImage(1, 2)); // 13 : up-down-left
        textures.add(sliceImage(2, 2)); // 14 : up-down-right
        textures.add(sliceImage(3, 2)); // 15 : down-left-right
        textures.add(sliceImage(4, 2)); // 16 : up-left-right
        textures.add(sliceImage(5, 2)); // 17 : up-down-left-right

        sheet = null;
    }

    private Image sliceImage(int col, int row) {
        return new WritableImage(
                sheet.getPixelReader(),
                col * FRAME_WIDTH,
                row * FRAME_HEIGHT,
                FRAME_WIDTH, FRAME_HEIGHT
        );
    }

    public Image getTexture(String key) {
        switch (key) {
            case "up":              return textures.get(11);
            case "down":            return textures.get(12);
            case "left":            return textures.get(6);
            case "right":           return textures.get(2);

            case "updown1":         return textures.get(9);
            case "updown2":         return textures.get(10);
            case "upleft":          return textures.get(4);
            case "upright":         return textures.get(3);
            case "downleft":        return textures.get(7);
            case "downright":       return textures.get(5);
            case "leftright1":      return textures.get(0);
            case "leftright2":      return textures.get(1);

            case "updownleft":      return textures.get(13);
            case "updownright":     return textures.get(14);
            case "upleftright":     return textures.get(16);
            case "downleftright":   return textures.get(15);

            case "updownleftright": return textures.get(17);
            case "single":          return textures.get(8);

            default:                throw new RuntimeException(key);
        }
    }
}
