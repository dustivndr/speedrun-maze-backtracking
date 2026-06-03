package io.github.maze.game;

/*
* Actual Game
*
*/

import io.github.maze.entities.Player;
import io.github.maze.maze.GameObject;
import io.github.maze.maze.TileManager;
import io.github.maze.obstacles.Obstacle;
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
    public static final int SCALE = 2;

    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 600;

    public static final int ROW_WIDTH = 40;
    public static final int COL_HEIGHT = 30;

    public List<GameObject> objectList = new ArrayList<>();

    public int FPS = 30;

    final Game game;
    public final Canvas canvas;
    public final GraphicsContext gc;
    public final TileManager tileManager;
    public final Player player;
    public Thread gameThread;

    public GamePanel(Game game) {
        this.game = game;

        canvas = new Canvas(SCREEN_WIDTH, SCREEN_HEIGHT);
        gc = canvas.getGraphicsContext2D();

        tileManager = new TileManager();
        player = new Player(this);

        getChildren().add(canvas);
    }

    public void startGameThread() {
        gameThread = new Thread(this);
    }

    // MAIN GAME LOOP
    @Override
    public void run() {

        long drawInterval = 1000000000 / FPS; // (1 sec in nanoseconds) / FPS
        long delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (true) {

            currentTime = System.nanoTime();                    // dapetin waktu sekarang dlm nanosecond spy akurat
            delta += (currentTime - lastTime) / drawInterval;   // jarak waktu antara tiap frame
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                render();
            }

            try {
                Thread.sleep(1_000_000_000 / FPS);
            } catch (InterruptedException e) {
            }
        }
    }

    private void drawMap() {

        Image grass = tileManager.getTile(1, 5);

        ImageView imageView = new ImageView(grass);

        for (int y = 0; y < SCREEN_HEIGHT; y += TILE_SIZE) {
            for (int x = 0; x < SCREEN_WIDTH; x += TILE_SIZE) {

                gc.drawImage(grass, x, y);
            }
        }

        imageView.setSmooth(false);

        getChildren().add(imageView);

    }

    public void drawObjects(GraphicsContext g) {
        objectList.sort(Comparator.comparing(GameObject::getDepth));
        for (int i = 0; i < objectList.size(); i++) {
            GameObject o = objectList.get(i);
            o.render(g);
        }
    }

    public void update() {

        for (int i = objectList.size() - 1; i >= 0; i--) {
            GameObject obj = objectList.get(i);
            obj.update();

            // handles obstacles
            if (obj instanceof Obstacle o) {
                if (o.removeObject()) {
                    objectList.remove(i);
                }
            }
        }
    }

    public void render() {

        GraphicsContext g = canvas.getGraphicsContext2D();

        drawMap();
        drawObjects(g);
    }

}
