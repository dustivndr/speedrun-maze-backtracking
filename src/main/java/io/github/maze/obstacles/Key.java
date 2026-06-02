package io.github.maze.obstacles;

import io.github.maze.game.GamePanel;
import javafx.scene.canvas.GraphicsContext;

public class Key extends Obstacle {

    private double dy;

    public Key(GamePanel gp, int col, int row) {
        super(gp,
                col * GamePanel.SCALE,
                row * GamePanel.SCALE,
                GamePanel.TILE_SIZE * GamePanel.SCALE,
                GamePanel.TILE_SIZE * GamePanel.SCALE);

        dy = 0;
    }

    @Override
    public double getDepth() { return y + height; }

    @Override
    public void render(GraphicsContext g) {

    }

    @Override
    public void update() {

    }
}
