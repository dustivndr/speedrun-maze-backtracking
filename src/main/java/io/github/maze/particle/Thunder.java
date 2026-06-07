package io.github.maze.particle;

import io.github.maze.game.GamePanel;
import io.github.maze.maze.GameObject;
import javafx.scene.canvas.GraphicsContext;

public class Thunder extends Particle {

    ThunderAssets thunderAssets;

    int frameCounter = 0;

    public Thunder(GamePanel gp, double centerX, double bottomY) {
        super(gp,
                centerX - ThunderAssets.FRAME_WIDTH / 2.0,
                bottomY - ThunderAssets.FRAME_HEIGHT,
                ThunderAssets.FRAME_WIDTH,
                ThunderAssets.FRAME_HEIGHT,
                6);

        thunderAssets = new ThunderAssets();

    }

    @Override
    public void render(GraphicsContext g) {
        double screenX = gp.camera.getScreenX(getX());
        double screenY = gp.camera.getScreenY(getY());
        g.drawImage(thunderAssets.getFrame(frameCounter), screenX, screenY);
    }

    @Override
    public void onUpdate() {
        frameCounter++;
    }

    @Override public double getDepth() { return getY() + getHeight(); }
}
