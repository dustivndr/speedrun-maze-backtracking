package io.github.maze.entities.projectile;

import io.github.maze.audio.SoundManager;
import io.github.maze.entities.Projectile;
import io.github.maze.game.GamePanel;
import io.github.maze.util.Angle;
import javafx.scene.canvas.GraphicsContext;

public class Meteor extends Projectile {

    MeteorAssets meteorAssets;
    private int frameNum = 0;

    private static final Angle angle = new Angle(Math.toRadians(135));

    private final double targetX;
    private final double targetY;
    private static final double SPEED = 18;

    private static final double SPEED_X = SPEED * Math.cos(angle.getRadians());
    private static final double SPEED_Y = SPEED * Math.sin(angle.getRadians());

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
        double startY = getStartY();

        // 1. Calculate how many pixels the meteor must travel vertically
        // We match the exact math used in checkDelete()
        double spawnCenterY = startY + MeteorAssets.FRAME_METEOR_CENTER_Y;
        double deltaY = targetY - spawnCenterY;

        // 2. Because it travels at 135 degrees, for every 1 pixel it goes DOWN,
        // it goes exactly 1 pixel to the LEFT (since cos(135)/sin(135) = -1).
        // Therefore, it must spawn exactly deltaY pixels to the RIGHT of the target.
        double centerStartX = targetX + deltaY;

        // 3. Offset back to the top-left corner of the sprite box
        return centerStartX - MeteorAssets.FRAME_METEOR_CENTER_X;
    }

    private static double getStartY() {
        return -MeteorAssets.FRAME_METEOR_HEIGHT;
    }

    @Override
    public void onUpdate() {
        SoundManager.METEOR_SFX.play();

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
    public void onDelete() {

        gp.maze.addMeteorImpactFrame(targetX, targetY);
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(meteorAssets.getFrame(frameNum), gp.camera.getScreenX(x), gp.camera.getScreenY(y));
    }

    @Override
    public double getDepth() {
        return targetY + height;
    }

    @Override
    public boolean getCollision() { return false; }

}
