package io.github.maze.obstacles;

import io.github.maze.game.GamePanel;
import javafx.scene.canvas.GraphicsContext;

public class Portal extends Obstacle {

    private static final PortalAssets portalAssets = new PortalAssets();

    private int frameNum = 0;
    private int animationTimer = 0;

    private Portal connection;
    private int portalNumber;

    public Portal(GamePanel gp, double x, double y, int num) {
        super(gp, x, y, GamePanel.TILE_SIZE * 2, GamePanel.TILE_SIZE);

        portalNumber = num;
    }

    @Override
    public void update() {

        final int gap = 3;
        animationTimer++;
        if (animationTimer >= gap) {
            frameNum = (frameNum + 1) % portalAssets.size();
            animationTimer -= gap;
        }
    }

    @Override
    public void render(GraphicsContext gc) {

        double screenX = gp.camera.getScreenX(x - GamePanel.TILE_SIZE / 2.0);
        double screenY = gp.camera.getScreenY(y - GamePanel.TILE_SIZE * 5 / 4.0);

        gc.drawImage(portalAssets.getFrame(frameNum), screenX, screenY);
    }

    @Override public boolean getCollision() { return false; }
    @Override public double getDepth() {
        return y + GamePanel.TILE_SIZE * 3 / 4.0;
    }

    public int getNum() { return portalNumber; }
    public void setConnection(Portal portal) { connection = portal; }
}
