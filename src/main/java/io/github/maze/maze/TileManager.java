package io.github.maze.maze;

/*
* Loader for Tileset
*
*/

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

import java.util.Objects;

public class TileManager {

    private final Image tileset;

    public TileManager() {

        var url = getClass().getResource(
                "/image/tiles/gentle-forest-v01.png"
        );

        System.out.println(url);

        tileset = new Image(
                Objects.requireNonNull(getClass().getResourceAsStream(
                        "/image/tiles/gentle-forest-v01.png"
                ))
        );

    }

    public Image getTile(int col, int row) {

        if (col < 0 || col > 7 || row < 0 || row > 7) {
            throw new IllegalArgumentException(
                    "Invalid tile coordinate: (" + col + ", " + row + ")"
            );
        }

        return new WritableImage(
                tileset.getPixelReader(),
                col * 16,
                row * 16,
                16,
                16
        );
    }

}
