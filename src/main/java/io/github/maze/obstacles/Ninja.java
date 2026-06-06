package io.github.maze.obstacles;

import io.github.maze.entities.Entity;
import io.github.maze.entities.Player;
import io.github.maze.game.GamePanel;
import javafx.scene.canvas.GraphicsContext;

public class Ninja extends Obstacle {

    NinjaAssets ninjaAssets = new NinjaAssets();
    Player p;

    final int damage = 5;

    int playerInsideWalkCounter = 0;

    public Ninja(GamePanel gp, double x, double y, double width, double height) {
        super(gp, x, y, width, height);
        p = gp.player;
    }

    @Override
    public void update() {

        if (
                p.getX() >= x - GamePanel.TILE_SIZE &&
                p.getX() <= x + GamePanel.TILE_SIZE &&
                p.getY() >= y - GamePanel.TILE_SIZE &&
                p.getY() <= y + GamePanel.TILE_SIZE
        ) {
            if (playerInsideWalkCounter % 2 == 0) {
                p.damage(damage);
            }

            playerInsideWalkCounter++;

        } else {
            playerInsideWalkCounter = 0;
        }
    }

    @Override
    public void render(GraphicsContext g) {

    }

    @Override public double getDepth() { return y + height; }
    @Override public boolean getCollision() { return true; }
}
