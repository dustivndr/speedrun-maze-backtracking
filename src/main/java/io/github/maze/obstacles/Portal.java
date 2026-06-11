package io.github.maze.obstacles;

import io.github.maze.audio.SoundManager;
import io.github.maze.game.GamePanel;
import io.github.maze.util.Util;
import javafx.scene.canvas.GraphicsContext;

public class Portal extends Obstacle {

    private static final PortalAssets portalAssets = new PortalAssets();

    private int frameNum = 0;
    private int animationTimer = 0;

    private Portal connection;
    private int portalNumber;

    protected int state = NONE;

    private static final int NONE = 0;
    private static final int EXITED = 1;
    private static final int ENTERED = 2;

    public Portal(GamePanel gp, double x, double y, int num) {
        super(gp, x, y, GamePanel.TILE_SIZE, GamePanel.TILE_SIZE);

        portalNumber = num;
    }

    @Override
    public void update() {

        // player exits from the portal
        if (state == EXITED) {
            if (!Util.checkAABB(gp.maze.player, this)) {
                state = NONE;
            }
        }

        // player teleports from this portal to the connection portal
        if (state == NONE && Util.checkAABB(this, gp.maze.player)) {
            connection.state = EXITED;

            gp.maze.player.setX(connection.x);
            gp.maze.player.setY(connection.y);

            SoundManager.PORTAL_SFX.play();
        }

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

    public Portal getConnection() { return connection; }
}
