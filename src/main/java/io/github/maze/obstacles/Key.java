package io.github.maze.obstacles;

import io.github.maze.game.Game;
import io.github.maze.game.GamePanel;
import javafx.scene.canvas.GraphicsContext;

public class Key extends Obstacle {

    int dy;
    int minDY = 0; // inclusive
    int maxDY = 10; // exclusive

    boolean dirUp = true;

    private long lastTime;
    private final int animationSpeedMs = 30;

    public Key(GamePanel gp, int col, int row) {
        super(gp,
                col * GamePanel.SCALE,
                row * GamePanel.SCALE,
                GamePanel.TILE_SIZE * GamePanel.SCALE,
                GamePanel.TILE_SIZE * GamePanel.SCALE);

        dy = 0;
        lastTime = System.currentTimeMillis();
    }

    @Override
    public double getDepth() { return y + height; }

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

        int screenX = (int) (this.x);
        int screenY = (int) (this.y - GamePanel.TILE_SIZE / 4.0 + dy);

        g.drawImage(texture, screenX, screenY, null);
    }

    @Override
    public void update() {

    }
}
