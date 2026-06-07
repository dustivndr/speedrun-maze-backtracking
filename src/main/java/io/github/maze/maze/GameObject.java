package io.github.maze.maze;

import io.github.maze.game.GamePanel;
import javafx.scene.canvas.GraphicsContext;

public interface GameObject {

    int BACKGROUND_OBJECT = -1;
    int FOREGROUND_OBJECT = GamePanel.TILE_SIZE * GamePanel.ROW_HEIGHT + 1;

    void render(GraphicsContext g);
    void update();
    double getDepth();
    double getY();
    double getX();
    double getHeight();
    double getWidth();

    void setX(double x);
    void setY(double y);
    void setHeight(double height);
    void setWidth(double width);

    boolean getCollision();
}
