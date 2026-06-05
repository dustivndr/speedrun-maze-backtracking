package io.github.maze.game;

/*
* Actual Game
*
*/

import io.github.maze.entities.Player;
import io.github.maze.input.InputHandler;
import io.github.maze.maze.GameObject;
import io.github.maze.maze.Maze;
import io.github.maze.maze.TileManager;
import io.github.maze.obstacles.Obstacle;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GamePanel extends Pane {

    public static final int ORIGINAL_TILE_SIZE = 16;
    public static final int TILE_SIZE;
    public static final int SCALE;

    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 600;

    public static final int ROW_WIDTH = 40;
    public static final int COL_HEIGHT = 30;

    static {
        SCALE = 2;
        TILE_SIZE = ORIGINAL_TILE_SIZE * SCALE;
    }

    public int FPS = 30;

    final Game game;
    public final Maze maze;
    public final Canvas canvas;
    public final GraphicsContext gc;
    public final TileManager tileManager;
    public final InputHandler inputHandler;
    public Player player;
    public AnimationTimer gameTimer;

    public GamePanel(Game game) {
        this.game = game;

        canvas = new Canvas(SCREEN_WIDTH, SCREEN_HEIGHT);
        gc = canvas.getGraphicsContext2D();

        tileManager = new TileManager();
        maze = new Maze(this);
        inputHandler = new InputHandler();
        setOnKeyPressed(inputHandler::keyPressed);
        setOnKeyReleased(inputHandler::keyReleased);

        setFocusTraversable(true);
        requestFocus();

        getChildren().add(canvas);

        setup();
    }

    public void startGameThread() {
        long drawInterval = 1_000_000_000 / FPS;

        gameTimer = new AnimationTimer() {
            private long lastTime = 0;
            private double delta = 0;


            // MAIN GAME LOOP
            @Override
            public void handle(long currentTime) {
                if (lastTime == 0) {
                    lastTime = currentTime;
                    return;
                }

                delta += (double) (currentTime - lastTime) / drawInterval;
                lastTime = currentTime;

                while (delta >= 1) {
                    update();
                    delta--;
                }

                render();
            }
        };

        gameTimer.start();
    }


    // TODO: TEMPORARY METHOD TO ADD OBJECTS
    void setup() {
        // SPIKE
//        maze.addObject(2, 1, 1);
//        maze.addObject(2, 1, 2);
//        maze.addObject(2, 2, 1);
//        maze.addObject(2, 2, 2);

        // KEY
        maze.addObject(4, 6, 3);

        // BUSH WALL
        maze.addObject(1, 0, 1);

        maze.addObject(1, 2, 1);
        maze.addObject(1, 2, 2);
        maze.addObject(1, 2, 3);
        maze.addObject(1, 3, 1);
        maze.addObject(1, 3, 2);
        maze.addObject(1, 3, 3);
        maze.addObject(1, 4, 1);
        maze.addObject(1, 4, 2);
        maze.addObject(1, 4, 3);

        maze.addObject(1, 2, 6);
        maze.addObject(1, 2, 7);
        maze.addObject(1, 3, 6);
        maze.addObject(1, 3, 7);

        // add player
        maze.addObject(5, 0, 0);
    }

    private void drawMap() {

        Image grass = tileManager.getTile(1, 5);

        for (int row = 0; row < COL_HEIGHT; row++) {
            for (int col = 0; col < ROW_WIDTH; col++) {

                gc.drawImage(grass, col * TILE_SIZE, row * TILE_SIZE);
            }
        }
    }

    public void drawObjects(GraphicsContext g) {
        maze.objectList.sort(Comparator.comparing(GameObject::getDepth));
        for (int i = 0; i < maze.objectList.size(); i++) {
            GameObject o = maze.objectList.get(i);
            o.render(g);
        }
    }

    public void update() {

        for (int i = maze.objectList.size() - 1; i >= 0; i--) {
            GameObject obj = maze.objectList.get(i);
            obj.update();

            // handles obstacles
            if (obj instanceof Obstacle o) {
                if (o.removeObject()) {
                    maze.objectList.remove(i);
                }
            }
        }
    }

    public void render() {

        drawMap();
        drawObjects(gc);
    }

}
