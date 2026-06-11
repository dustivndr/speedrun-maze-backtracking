package io.github.maze.game;

/*
* Actual Game
*
*/

import java.util.Comparator;
import java.util.Objects;

import io.github.maze.input.InputHandler;
import io.github.maze.maze.GameObject;
import io.github.maze.maze.Maze;
import io.github.maze.maze.TileManager;
import io.github.maze.maze.loader.MazeLoader;
import io.github.maze.maze.solver.MazeSolver;
import io.github.maze.obstacles.Obstacle;
import io.github.maze.render.Camera;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class GamePanel extends Pane {

    public static final int ORIGINAL_TILE_SIZE = 16;
    public static final int TILE_SIZE;
    public static final int SCALE;

    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 600;

    public static final int COL_WIDTH = 30;
    public static final int ROW_HEIGHT = 20;

    public static final int WORLD_WIDTH;
    public static final int WORLD_HEIGHT;

    static {
        SCALE = 2;
        TILE_SIZE = ORIGINAL_TILE_SIZE * SCALE; // 32

        WORLD_WIDTH = TILE_SIZE * COL_WIDTH;
        WORLD_HEIGHT = TILE_SIZE * ROW_HEIGHT;
    }

    public int FPS = 30;

    public final Game game;
    public final Maze maze;
    public final Canvas canvas;
    public final GraphicsContext gc;
    public final TileManager tileManager;
    public final InputHandler inputHandler;
    public final UI ui;
    public final Camera camera;
    public final MazeLoader mazeLoader;
    public AnimationTimer gameTimer;
    private AutoPlayer autoPlayer;

    private String path;

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

        path = MazeSolver.solve(this);

        ui = new UI(this);

        String cssPath = Objects.requireNonNull(getClass().getResource("/styles/styles.css")).toExternalForm();
        this.getStylesheets().add(cssPath);

        setFocusTraversable(true);
        requestFocus();

    }

    public void startGameThread(String state) {

        long drawInterval = 1_000_000_000 / FPS;
        if (state.equals("showBacktracking")) {
            path = MazeSolver.backtrackingPath.toString();
        }

        autoPlayer = new AutoPlayer(
                this,
                path
        );

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

        if(autoPlayer != null) {
            autoPlayer.update();
        }

        maze.player.update();

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