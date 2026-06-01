package io.github.maze.obstacles;

/*
* ABSTRACT CLASS OBSTACLES
*
*/

import io.github.maze.entities.Player;
import javafx.scene.canvas.GraphicsContext;

public abstract class Obstacle {

    protected int x;
    protected int y;

    public Obstacle(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public abstract void update(Player player);

    public abstract void render(GraphicsContext gc);

}
