package io.github.maze.render;

import io.github.maze.game.GamePanel;
import io.github.maze.maze.GameObject;

public class Camera {

    double x;
    double y;

    final GamePanel gp;
    final GameObject lock;

    public Camera(GamePanel gp, GameObject lock) {
        this.gp = gp;
        this.lock = lock;
    }

    public void setPos() {
        // the top-leftmost position to center the lock (usually player)
        x = lock.getX() - (GamePanel.SCREEN_WIDTH / 2.0);
        y = lock.getY() - (GamePanel.SCREEN_HEIGHT / 2.0);

        // clamps X to left right bounds
        if (x < 0) {
            x = 0;
        }
        if (x > GamePanel.COL_WIDTH * GamePanel.TILE_SIZE - GamePanel.SCREEN_WIDTH) {
            x = GamePanel.COL_WIDTH * GamePanel.TILE_SIZE - GamePanel.SCREEN_WIDTH;
        }

        // clamps Y to left right bounds
        if (y < 0) {
            y = 0;
        }
        if (y > GamePanel.ROW_HEIGHT * GamePanel.TILE_SIZE - GamePanel.SCREEN_HEIGHT) {
            y = GamePanel.ROW_HEIGHT * GamePanel.TILE_SIZE - GamePanel.SCREEN_HEIGHT;
        }
    }

    public double getScreenX(double x) {
        return x - this.x;
    }

    public double getScreenY(double y) {
        return y - this.y;
    }
}
