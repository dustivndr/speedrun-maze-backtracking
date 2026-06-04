package io.github.maze.game;

/*
* Actual Game
*
*/

import io.github.maze.entities.Player;
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

public class GamePanel extends Pane implements Runnable {

    public static final int TILE_SIZE = 16;
    public static final int SCALE = 1;

    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 600;

    public static final int ROW_WIDTH = 40;
    public static final int COL_HEIGHT = 30;


    public int FPS = 30;

    final Game game;
    public final Maze maze;
    public final Canvas canvas;
    public final GraphicsContext gc;
    public final TileManager tileManager;
    public final Player player;
    public AnimationTimer gameTimer;

    public GamePanel(Game game) {
        this.game = game;

        canvas = new Canvas(SCREEN_WIDTH, SCREEN_HEIGHT);
        gc = canvas.getGraphicsContext2D();

        tileManager = new TileManager();
        player = new Player(this);
        maze = new Maze(this);

        getChildren().add(canvas);

        setup();
    }

    public void startGameThread() {
        long drawInterval = 1_000_000_000 / FPS;

        gameTimer = new AnimationTimer() {
            private long lastTime = 0;
            private double delta = 0;

            @Override
            public void handle(long currentTime) {
                if (lastTime == 0) {
                    lastTime = currentTime;
                    return;
                }

                // Delta accumulation
                delta += (double) (currentTime - lastTime) / drawInterval;
                lastTime = currentTime;

                // Game updates (Updates at capped 30 FPS)
                while (delta >= 1) {
                    update();
                    delta--;
                }

                // Render happens safely on the JavaFX UI Thread
                render();
            }
        };

        gameTimer.start();
    }


    // TODO: TEMPORARY METHOD TO ADD OBJECTS
    void setup() {
        maze.addObject(2, 1, 1);
        maze.addObject(4, 2, 2);
    }

    // MAIN GAME LOOP
    @Override
    public void run() {

        long drawInterval = 1_000_000_000 / FPS; // (1 sec in nanoseconds) / FPS
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (true) {

            currentTime = System.nanoTime();                    // dapetin waktu sekarang dlm nanosecond spy akurat
            delta += (currentTime - lastTime) / (double) drawInterval;   // jarak waktu antara tiap frame
            lastTime = currentTime;

            while (delta >= 1) {
                update();
                render();

                delta--;
            }

            try {
                Thread.sleep(1000 / FPS);
            } catch (InterruptedException e) {
                System.exit(0);
            }
        }
    }

    private void drawMap() {

        Image grass = tileManager.getTile(1, 5);

        for (int row = 0; row < COL_HEIGHT; row++) {
            for (int col = 0; col < ROW_WIDTH; col++) {

                gc.drawImage(grass, col, row);
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
