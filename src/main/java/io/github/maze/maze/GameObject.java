package io.github.maze.maze;

import javafx.scene.canvas.GraphicsContext;

public interface GameObject {

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
