package io.github.maze.obstacles;

import io.github.maze.entities.Player;
import io.github.maze.game.GamePanel;
import io.github.maze.util.Util;
import javafx.scene.canvas.GraphicsContext;

public class Fire extends Obstacle {

    final FireAssets fireAssets = new FireAssets();
    final Player p;

    int animationCounter = 0;
    long lastTime;

    public Fire(GamePanel gp, double x, double y) {
        super(gp, x, y, GamePanel.TILE_SIZE, GamePanel.TILE_SIZE);
        p = gp.player;
        lastTime = System.currentTimeMillis();
    }

    @Override
    public void render(GraphicsContext g) {

    }

    @Override
    public void update() {
        // check damage player
        Player p = gp.player;
        if (Util.checkAABB(p, this)) {
//            p.damage();
        }

        // update texture
        long currTime = System.currentTimeMillis();
        long animationTimer = 200;
        if (currTime - lastTime >= animationTimer) {
            lastTime = currTime;
            animationCounter = ++animationCounter % fireAssets.size();
        }
    }

    @Override public double getDepth() { return y + height; }
    @Override public boolean getCollision() { return false; }
}
