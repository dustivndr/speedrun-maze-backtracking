package io.github.maze.particle;

import io.github.maze.game.GamePanel;
import io.github.maze.maze.GameObject;

public abstract class Particle implements GameObject {

    GamePanel gp;

    private double x;
    private double y;
    private double width;
    private double height;
    private int life;
    private int timerMs;

    public Particle(GamePanel gp, double x, double y, double width, double height, int lifetime) {
        this.gp = gp;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.life = lifetime;
    }

    public int getLife() { return life; }
    @Override public double getX() { return x; }
    @Override public double getY() { return y; }
    @Override public double getWidth() { return width; }
    @Override public double getHeight() { return height; }

    @Override public void setX(double x) { this.x = x; }
    @Override public void setY(double y) { this.y = y; }
    @Override public void setWidth(double width) { this.width = width; }
    @Override public void setHeight(double height) { this.height = height; }

    @Override public boolean getCollision() { return false; }
    @Override public double getDepth() { return GameObject.PARTICLE_LAYER; }

    @Override
    public void update() {

        life--;
        onUpdate();

        if (life <= 0) {
            selfDelete();
        }
    }

    public abstract void onUpdate();

    private void selfDelete() {
        gp.maze.particleList.remove(this);
    }
}
