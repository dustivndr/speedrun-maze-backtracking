package io.github.maze.obstacles;

/*
* ABSTRACT CLASS OBSTACLES
*
*/

import io.github.maze.entities.Player;
import io.github.maze.game.GamePanel;
import io.github.maze.maze.GameObject;
import javafx.scene.canvas.GraphicsContext;

public abstract class Obstacle implements GameObject {

    final GamePanel gp;
    protected double x;
    protected double y;
    protected double width;
    protected double height;

    public Obstacle(GamePanel gp, double x, double y, double width, double height) {
        this.gp = gp;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override public double getX() { return x; }
    @Override public double getY() { return y; }
    @Override public double getWidth() { return width; }
    @Override public double getHeight() { return height; }

    @Override public void setX(double x) { this.x = x; }
    @Override public void setY(double y) { this.y = y; }
    @Override public void setWidth(double width) { this.width = width; }
    @Override public void setHeight(double height) { this.height = height; }
}
