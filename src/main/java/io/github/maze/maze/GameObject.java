package io.github.maze.maze;

import javafx.scene.canvas.GraphicsContext;

public interface GameObject {

    public void draw(GraphicsContext g);
    public double getDepth();
    public double getY();
    public double getX();
    public double getHeight();
    public double getWidth();

    public void setX(double x);
    public void setY(double y);
    public void setHeight(double height);
    public void setWidth(double width);
}
