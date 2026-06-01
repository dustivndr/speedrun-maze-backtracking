package io.github.maze.game;

/*
* Actual Game
*
*/

import io.github.maze.maze.TileManager;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class GamePanel extends Pane {

    public final class Constants {

        public static final int TILE_SIZE = 16;

        public static final int SCREEN_WIDTH = 800;
        public static final int SCREEN_HEIGHT = 600;

        private Constants() {}
    }

    private final Canvas canvas;
    private final GraphicsContext gc;
    private final TileManager tileManager;

    public GamePanel(Game game) {

        //initializeGame();

        canvas = new Canvas(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        gc = canvas.getGraphicsContext2D();

        tileManager = new TileManager();

        getChildren().add(canvas);

        drawMap();

    }

//    private void initializeGame() {
//
//    }

    private void drawMap() {

        Image grass = tileManager.getTile(1, 5);

        ImageView imageView = new ImageView(grass);

        for (int y = 0; y < Constants.SCREEN_HEIGHT; y += Constants.TILE_SIZE) {
            for (int x = 0; x < Constants.SCREEN_WIDTH; x += Constants.TILE_SIZE) {

                gc.drawImage(grass, x, y);
            }
        }

        imageView.setSmooth(false);

        getChildren().add(imageView);

    }

    public void update() {

    }

    public void render() {

    }

}
