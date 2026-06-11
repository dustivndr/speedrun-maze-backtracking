package io.github.maze.obstacles;

import io.github.maze.audio.SoundManager;
import io.github.maze.game.GamePanel;
import io.github.maze.util.Util;
import javafx.scene.canvas.GraphicsContext;

public class FlagGreen extends Obstacle {

    final FlagGreenAssets flagGreenAssets = new FlagGreenAssets();

    public FlagGreen(GamePanel gp, double x, double y) {
        super(gp, x, y, GamePanel.TILE_SIZE, GamePanel.TILE_SIZE);
    }

    @Override
    public void render(GraphicsContext g) {
        double screenX = gp.camera.getScreenX(x);
        double screenY = gp.camera.getScreenY(y);

        g.drawImage(flagGreenAssets.getFlagGreenImage(), screenX, screenY, GamePanel.TILE_SIZE, GamePanel.TILE_SIZE);
    }

    @Override
    public void update() {
        if (Util.checkAABB(gp.maze.player, this)) {
            SoundManager.GREEN_FLAG_SFX.play();

            gp.maze.player.flagCount++;
//            if (gp.maze.player.flagCount == gp.maze.flagCount) {
//                gp.game.finishGame();
//            }
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
