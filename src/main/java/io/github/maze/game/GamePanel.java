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
import io.github.maze.maze.loader.MazeLoader;
import io.github.maze.obstacles.Obstacle;
import io.github.maze.render.Camera;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

import java.util.Comparator;
import java.util.Objects;

public class GamePanel extends Pane {

    public static final int ORIGINAL_TILE_SIZE = 16;
    public static final int TILE_SIZE;
    public static final int SCALE;

    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 600;

    public static final int COL_WIDTH = 40;
    public static final int ROW_HEIGHT = 30;

    public static final int WORLD_WIDTH;
    public static final int WORLD_HEIGHT;

    static {
        SCALE = 2;
        TILE_SIZE = ORIGINAL_TILE_SIZE * SCALE;

        WORLD_WIDTH = TILE_SIZE * COL_WIDTH;
        WORLD_HEIGHT = TILE_SIZE * ROW_HEIGHT;
    }

    public int FPS = 30;

    final Game game;
    public final Maze maze;
    public final Canvas canvas;
    public final GraphicsContext gc;
    public final TileManager tileManager;
    public final InputHandler inputHandler;
    public final UI ui;
    public final Camera camera;
    public final MazeLoader mazeLoader;
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

        getChildren().add(canvas);

        mazeLoader = new MazeLoader(this);
        mazeLoader.loadNextMapPlayer();

        camera = new Camera(this, maze.player);
        mazeLoader.loadNextMapObstacles();

        ui = new UI(this);

        String cssPath = Objects.requireNonNull(getClass().getResource("/styles/styles.css")).toExternalForm();
        this.getStylesheets().add(cssPath);

        setFocusTraversable(true);
        requestFocus();
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


    // TODO: TEMPORARY METHOD TO ADD OBSTACLES LATER REPLACE WITH map.txt FILES
    void initGameObjects() {

        // ====== map 1 ========
//         SPIKE
//        maze.addObject(2, 5, 0);
//        maze.addObject(2, 5, 1);
//        maze.addObject(2, 6, 1);
//        maze.addObject(2, 6, 2);
//
//        // KEY
//        maze.addObject(4, 6, 3);
//
//        // BUSH WALL
//        maze.addObject(1, 0, 1);
//
//        maze.addObject(1, 2, 1);
//        maze.addObject(1, 2, 2);
//        maze.addObject(1, 2, 3);
//        maze.addObject(1, 3, 1);
//        maze.addObject(1, 3, 2);
//        maze.addObject(1, 3, 3);
//        maze.addObject(1, 4, 1);
//        maze.addObject(1, 4, 2);
//        maze.addObject(1, 4, 3);
//
//        maze.addObject(1, 2, 6);
//        maze.addObject(1, 2, 7);
//        maze.addObject(1, 3, 6);
//        maze.addObject(1, 3, 7);
//
//        // NINJA
//        maze.addObject(6, 6, 0);



        // ======== map 2 : GRASS DEBUG =========
//        maze.addObject(1, 4, 3);
//        maze.addObject(1, 5, 3);
//        maze.addObject(1, 6, 3);
//        maze.addObject(1, 7, 3);
//        maze.addObject(1, 8, 3);
//        maze.addObject(1, 4, 4);
//        maze.addObject(1, 6, 4);
//        maze.addObject(1, 8, 4);
//        maze.addObject(1, 4, 5);
//        maze.addObject(1, 5, 5);
//        maze.addObject(1, 6, 5);
//        maze.addObject(1, 7, 5);
//        maze.addObject(1, 8, 5);
//        maze.addObject(1, 4, 6);
//        maze.addObject(1, 6, 6);
//        maze.addObject(1, 8, 6);
//        maze.addObject(1, 4, 7);
//        maze.addObject(1, 5, 7);
//        maze.addObject(1, 6, 7);
//        maze.addObject(1, 7, 7);
//        maze.addObject(1, 8, 7);
//
//        maze.addObject(1, 10, 1);
//        maze.addObject(1, 11, 1);
//        maze.addObject(1, 12, 1);
//        maze.addObject(1, 10, 2);
//        maze.addObject(1, 11, 2);
//        maze.addObject(1, 12, 2);
//        maze.addObject(1, 10, 3);
//        maze.addObject(1, 11, 3);
//        maze.addObject(1, 12, 3);
//
//        maze.addObject(1, 4, 10);
//        maze.addObject(1, 4, 11);
//        maze.addObject(1, 4, 12);
//        maze.addObject(1, 4, 13);
//        maze.addObject(1, 4, 14);
//        maze.addObject(1, 2, 12);
//        maze.addObject(1, 3, 12);
//        maze.addObject(1, 5, 12);
//        maze.addObject(1, 6, 12);



        // ======== map 3 : HOLES ========
//        maze.addObject(3, 3, 3);
//        maze.addObject(3, 4, 3);
//        maze.addObject(3, 5, 3);
//        maze.addObject(3, 3, 4);
//        maze.addObject(3, 4, 4);
//        maze.addObject(3, 5, 4);
//        maze.addObject(3, 3, 6);
//        maze.addObject(3, 4, 6);
//        maze.addObject(3, 5, 6);


        // ========= map 4 : FIRE ==========
//        maze.addObject(7, 2, 2);
//        maze.addObject(7, 2, 3);
//        maze.addObject(7, 2, 4);
//        maze.addObject(7, 4, 2);
//        maze.addObject(7, 4, 3);
//        maze.addObject(7, 4, 4);


        // ========= map 5 : WIZARD =========
//        maze.addObject(8, 5, 5);
    }

    private void drawMap() {

        Image grass = tileManager.grass;

        for (int row = 0; row < ROW_HEIGHT; row++) {
            for (int col = 0; col < COL_WIDTH; col++) {

                gc.drawImage(grass,
                        camera.getScreenX(col * TILE_SIZE),
                        camera.getScreenY(row * TILE_SIZE));
            }
        }
    }

    public void drawObjects(GraphicsContext g) {
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

        camera.setPos();
        maze.objectList.sort(Comparator.comparing(GameObject::getDepth));

        drawMap();
        drawObjects(gc);
        ui.render();
    }

}
