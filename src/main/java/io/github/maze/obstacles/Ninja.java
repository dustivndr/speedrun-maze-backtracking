package io.github.maze.obstacles;

import io.github.maze.entities.Entity;
import io.github.maze.game.GamePanel;
import javafx.scene.canvas.GraphicsContext;

public class Ninja extends Obstacle {

    public Ninja(GamePanel gp, double x, double y, double width, double height) {
        super(gp, x, y, width, height);
    }

    private void getTextures() {

    }

    @Override
    public void update() {

    }

    @Override
    public void render(GraphicsContext g) {

    }

    @Override
    public double getDepth() { return y + height; }
}
