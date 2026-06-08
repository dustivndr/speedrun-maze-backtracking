package io.github.maze.obstacles;

import io.github.maze.entities.Player;
import io.github.maze.game.GamePanel;
import io.github.maze.render.Camera;
import io.github.maze.util.Util;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Rectangle;

public class Hole extends Obstacle {

    private static final HoleAssets holeAssets = new HoleAssets();

    boolean hasOpened = false;
    boolean collision = false;
    private final Camera camera;

    int animationCounter = 0;

    private boolean triggered = false;

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

        Player p = gp.maze.player;
        if (Util.checkAABB(p, this)) {

            if (!hasOpened) {
                p.damage(5);
            }

            hasOpened = true;
            collision = true;

        }
    }

    @Override public double getDepth() { return -1; }
    @Override public boolean getCollision() { return collision; }
}
