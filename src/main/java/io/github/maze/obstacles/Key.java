package io.github.maze.obstacles;

import io.github.maze.game.Game;
import io.github.maze.game.GamePanel;
import io.github.maze.render.Camera;
import io.github.maze.util.Util;
import javafx.scene.canvas.GraphicsContext;

public class Key extends Obstacle {

    int dy;
    int minDY = -5; // inclusive
    int maxDY = 0; // exclusive

    static final KeyAssets assets = new KeyAssets();
    private Camera camera;

    boolean dirUp = true;

    private long lastTime;
    private static final int animationSpeedMs = 60;

    public Key(GamePanel gp, int col, int row) {
        super(gp,
                col * GamePanel.TILE_SIZE,
                row * GamePanel.TILE_SIZE,
                GamePanel.TILE_SIZE,
                GamePanel.TILE_SIZE);

        dy = 0;
        lastTime = System.currentTimeMillis();
        camera = gp.camera;
    }

    @Override public double getDepth() { return y + height; }
    @Override public boolean getCollision() { return false; }

    @Override
    public void render(GraphicsContext g) {

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastTime >= animationSpeedMs) {

            // move the key down on px
            if (dirUp) {
                if (dy < maxDY) {
                    dy += 1;
                } else {
                    dirUp = false;
                    dy -= 1;
                }
            }

            else {
                if (dy >= minDY) {
                    dy -= 1;
                } else {
                    dirUp = true;
                    dy += 1;
                }
            }

            lastTime = currentTime;

        }

        double screenX = camera.getScreenX(this.x);
        double screenY = camera.getScreenY(this.y + dy - 5);

        g.drawImage(assets.getKeyImage(), screenX, screenY);

    }

    @Override
    public void update() {

        if (Util.checkAABB(this, gp.maze.player)) {
            gp.maze.player.keyCount++;
            removeObject = true;
        }
    }
}
