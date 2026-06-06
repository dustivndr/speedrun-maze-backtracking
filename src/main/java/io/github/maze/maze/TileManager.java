package io.github.maze.maze;

/*
* Loader for Tileset
*
*/

import io.github.maze.game.GamePanel;
import io.github.maze.util.Util;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

public class TileManager {

    private final Image tileset;
    public final Image grass;

    public TileManager() {
        tileset = Util.getScaledImage("/image/tiles/gentle-forest-v01.png");
        grass = getTile(1, 5);
    }

    public final Image getTile(int col, int row) {

        if (col < 0 || col > 7 || row < 0 || row > 7) {
            throw new IllegalArgumentException(
                    "Invalid tile coordinate: (" + col + ", " + row + ")"
            );
        }

        return new WritableImage(
                tileset.getPixelReader(),
                col * GamePanel.TILE_SIZE,
                row * GamePanel.TILE_SIZE,
                GamePanel.TILE_SIZE,
                GamePanel.TILE_SIZE
        );
    }

}
