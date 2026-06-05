package io.github.maze.maze;

/*
* Loader for Tileset
*
*/

import io.github.maze.game.GamePanel;
import io.github.maze.util.Util;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

import java.net.URL;
import java.util.Objects;

public class TileManager {

    private final Image tileset;

    public TileManager() {
        tileset = Util.getScaledImage("/image/tiles/gentle-forest-v01.png");
    }

    public Image getTile(int col, int row) {

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
