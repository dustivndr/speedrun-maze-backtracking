package io.github.maze.obstacles;

import io.github.maze.game.GamePanel;
import io.github.maze.util.Util;
import javafx.scene.canvas.GraphicsContext;

public class SpellHeal extends Obstacle {

    private static final SpellAssets spellAssets = new SpellAssets();

    int dy;
    int minDY = -5; // inclusive
    int maxDY = 0; // exclusive
    boolean dirUp = true;

    private long lastTime;
    private static final int animationSpeedMs = 100;

    public SpellHeal(GamePanel gp, double x, double y) {
        super(gp, x, y, GamePanel.TILE_SIZE, GamePanel.TILE_SIZE);
    }


    @Override
    public void render(GraphicsContext g) {

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastTime >= animationSpeedMs) {

            // move the key down on px
            if (dirUp) {
                if (dy < maxDY) {
                    dy += 1;
                } else {
                    dirUp = false;
                    dy -= 1;
                }
            }

            else {
                if (dy >= minDY) {
                    dy -= 1;
                } else {
                    dirUp = true;
                    dy += 1;
                }
            }

            lastTime = currentTime;

        }

        double screenX = gp.camera.getScreenX(this.x);
        double screenY = gp.camera.getScreenY(this.y + dy - 5);

        g.drawImage(spellAssets.getSpell(SpellAssets.HEAL), screenX, screenY);
    }

    @Override
    public void update() {

        if (Util.checkAABB(gp.maze.player, this)) {
            gp.maze.player.heal(10);
            removeObject = true;
        }
    }

    @Override
    public double getDepth() {
        return y + height;
    }

    @Override
    public boolean getCollision() {
        return false;
    }
}
