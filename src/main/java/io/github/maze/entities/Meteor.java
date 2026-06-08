package io.github.maze.entities;

import io.github.maze.game.GamePanel;
import io.github.maze.maze.GameObject;
import io.github.maze.particle.Particle;
import io.github.maze.util.Angle;
import javafx.scene.canvas.GraphicsContext;

public class Meteor extends Projectile {

    MeteorAssets meteorAssets;
    private int frameNum = 0;

    private static Angle angle = new Angle(Math.toRadians(110));

    private final double targetX;
    private final double targetY;
    private static final double SPEED = 18;

    private static final double SPEED_X = SPEED * Math.cos(angle.getRadians());
    private static final double SPEED_Y = SPEED * Math.cos(angle.getRadians());

    public Meteor(GamePanel gp, double targetX, double targetY) {
        super(gp,
                getStartX(targetX, targetY),
                getStartY(),
                MeteorAssets.FRAME_METEOR_WIDTH,
                MeteorAssets.FRAME_METEOR_HEIGHT);

        this.targetX = targetX;
        this.targetY = targetY;

        meteorAssets = new MeteorAssets();
    }

    private static double getStartX(double targetX, double targetY) {
        double angleRadians = Math.toRadians(110);
        double startY = getStartY();

        // find where the visual center line hits the spawn height
        double centerStartX = targetX - ((targetY - startY) / Math.tan(angleRadians));

        // offset back to the top-left corner of the sprite box
        return centerStartX - MeteorAssets.FRAME_METEOR_CENTER_X;
    }

    private static double getStartY() {
        return -MeteorAssets.FRAME_METEOR_HEIGHT;
    }

    @Override
    public void onUpdate() {
        x += SPEED_X;
        y += SPEED_Y;

        frameNum = (frameNum + 1) % meteorAssets.size();
    }

    @Override
    public boolean checkDelete() {
        double currentCenterY = y + MeteorAssets.FRAME_METEOR_CENTER_Y;

        return currentCenterY >= targetY;
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(meteorAssets.getFrame(frameNum), x, y);
    }

    @Override
    public double getDepth() {
        return y + height;
    }

    @Override
    public boolean getCollision() { return false; }

}
