package io.github.maze.particle;

import io.github.maze.game.GamePanel;
import io.github.maze.maze.GameObject;
import javafx.scene.canvas.GraphicsContext;

public class MeteorImpactFrame extends Particle {

    private static final MeteorImpactFrameAssets meteorImpactFrameAssets = new MeteorImpactFrameAssets();;

    int frameCounter = 0;
    int animationCounter = 0;
    static final int frameGap = 1;

    public MeteorImpactFrame(GamePanel gp, double centerX, double bottomY) {
        super(gp,
                centerX - MeteorImpactFrameAssets.CENTER_X,
                bottomY - MeteorImpactFrameAssets.CENTER_Y,
                MeteorImpactFrameAssets.FRAME_WIDTH,
                MeteorImpactFrameAssets.FRAME_HEIGHT,
                meteorImpactFrameAssets.size() * frameGap);

        gp.maze.player.damage(20);
    }

    @Override
    public void render(GraphicsContext g) {
        double screenX = gp.camera.getScreenX(getX());
        double screenY = gp.camera.getScreenY(getY());
        g.drawImage(meteorImpactFrameAssets.getFrame(frameCounter), screenX, screenY);
    }

    @Override
    public void onUpdate() {
        animationCounter++;
        frameCounter = animationCounter / frameGap;
    }

    @Override
    public double getDepth() {
        return GameObject.TOP_LAYER;
    }
}
