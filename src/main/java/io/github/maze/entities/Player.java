package io.github.maze.entities;

import io.github.maze.game.Game;
import io.github.maze.game.GamePanel;
import javafx.scene.canvas.GraphicsContext;

public class Player extends Entity {

    private int health;

    public int keyCount = 0;

    public Player(GamePanel gp) {
        this(gp, 0, 0);
    }

    public Player(GamePanel gp, double x, double y) {
        super(gp, x, y, GamePanel.TILE_SIZE, GamePanel.TILE_SIZE);
    }

    public void damage(int i) {
        health -= i;

        if (health <= 0)
            health = 0;
    }

    public int getTileX() {
        return (int) (x / GamePanel.TILE_SIZE);
    }

    public int getTileY() {
        return (int) (y / GamePanel.TILE_SIZE);
    }

    @Override
    public void render(GraphicsContext g) {

    }

    @Override
    public void update() {

    }

    @Override
    public double getDepth() {
        return 0;
    }
}
