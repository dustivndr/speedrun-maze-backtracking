package io.github.maze.entities;

import io.github.maze.game.Game;
import io.github.maze.game.GamePanel;

public abstract class Projectile extends Entity {

    protected Projectile(GamePanel gp, double x, double y, double width, double height) {
        super(gp, x, y, width, height);
    }

    @Override
    public void update() {
        onUpdate();

        if (checkDelete()) {
            onDelete();
            selfDelete();
        }
    }

    public void onDelete() {}
    abstract public void onUpdate();
    abstract public boolean checkDelete();

    private void selfDelete() {
        gp.maze.objectList.remove(this);
    }
}
