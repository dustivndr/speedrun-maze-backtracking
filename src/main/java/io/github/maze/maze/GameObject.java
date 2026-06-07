package io.github.maze.maze;

import io.github.maze.game.GamePanel;
import javafx.scene.canvas.GraphicsContext;

public interface GameObject {

    public static final int BACKGROUND_OBJECT = -1;
    public static final int FOREGROUND_OBJECT = GamePanel.TILE_SIZE * GamePanel.ROW_HEIGHT + 1;
    public static final int PARTICLE_LAYER = FOREGROUND_OBJECT + 1;

    public void render(GraphicsContext g);
    public void update();
    public double getDepth();
    public double getY();
    public double getX();
    public double getHeight();
    public double getWidth();

    public void setX(double x);
    public void setY(double y);
    public void setHeight(double height);
    public void setWidth(double width);

    public boolean getCollision();
}
