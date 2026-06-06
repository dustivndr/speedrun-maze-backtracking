package io.github.maze.obstacles;

import io.github.maze.entities.Player;
import io.github.maze.game.GamePanel;
import io.github.maze.render.Camera;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Rectangle;

public class Hole extends Obstacle {

    private static HoleAssets holeAssets = new HoleAssets();

    boolean hasOpened = false;
    boolean collision = false;
    private Camera camera;

    int animationCounter = 0;

    long lastTime = 0;

    public Hole(GamePanel gp, int col, int row) {
        super(gp,
                col * GamePanel.TILE_SIZE,
                row * GamePanel.TILE_SIZE,
                GamePanel.TILE_SIZE, GamePanel.TILE_SIZE);
        camera = gp.camera;
    }

    @Override
    public void render(GraphicsContext gc) {

        if (!hasOpened) return;
        gc.drawImage(holeAssets.getImage(animationCounter), camera.getScreenX(x), camera.getScreenY(y));

        long curr = System.currentTimeMillis();
        final long animationTimerMs = 100;
        if (animationCounter < 2 &&
                curr - lastTime > animationTimerMs) {
            lastTime = curr;
            animationCounter++;
        }
    }

    @Override
    public void update() {

        int col = (int) (x / GamePanel.TILE_SIZE);
        int row = (int) (y / GamePanel.TILE_SIZE);

        Player p = gp.player;
        if (
                p.getX() < x + width &&
                p.getX() + p.getWidth() > x &&
                p.getY() < y + height &&
                p.getY() + p.getHeight() > y
        ) {
            hasOpened = true;
            collision = true;
        }
    }

    @Override public double getDepth() { return -1; }
    @Override public boolean getCollision() { return collision; }
}
