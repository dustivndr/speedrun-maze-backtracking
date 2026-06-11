package io.github.maze.obstacles;

import io.github.maze.audio.SoundManager;
import io.github.maze.entities.Player;
import io.github.maze.game.GamePanel;
import io.github.maze.util.Util;
import javafx.scene.canvas.GraphicsContext;

public class Fire extends Obstacle {

    private static final FireAssets fireAssets = new FireAssets();
    final Player p;

    int animationCounter = 0;
    long lastTime;
    int damage = 5;

    boolean playerWasInside = false;

    public Fire(GamePanel gp, double x, double y) {
        super(gp, x, y, GamePanel.TILE_SIZE, GamePanel.TILE_SIZE);
        p = gp.maze.player;
        lastTime = System.currentTimeMillis();
    }

    @Override
    public void render(GraphicsContext g) {
        double screenX = gp.camera.getScreenX(x);
        double screenY = gp.camera.getScreenY(y);

        g.drawImage(fireAssets.getFrame(animationCounter), screenX, screenY);
    }

    @Override
    public void update() {

        // check damage player
        Player p = gp.maze.player;
        if (Util.checkAABB(p, this)) {
            if (!playerWasInside) {
                p.damage(damage);
                playerWasInside = true;
            }
        } else {
            playerWasInside = false;
        }

        // update texture
        long currTime = System.currentTimeMillis();
        long animationTimer = 200;
        if (currTime - lastTime >= animationTimer) {
            lastTime = currTime;
            animationCounter = ++animationCounter % fireAssets.size();
        }

        SoundManager.FIRE_SFX.play();

    }

    @Override public double getDepth() { return y + height; }
    @Override public boolean getCollision() { return false; }
}
