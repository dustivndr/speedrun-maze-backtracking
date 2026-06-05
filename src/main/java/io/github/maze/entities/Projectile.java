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
            selfDelete();
        }
    }

    abstract protected void onUpdate();
    abstract protected boolean checkDelete();

    private void selfDelete() {
        gp.maze.objectList.remove(this);
    }
}
