package io.github.maze.entities;

import io.github.maze.game.Game;
import io.github.maze.game.GamePanel;
import javafx.scene.canvas.GraphicsContext;

public class Player extends Entity {

    private int health;

    public int keyCount = 0;

    private int frameCounter = 0;
    private String direction = "down";
    private boolean isMoving = false;

    private static StringBuilder sb = new StringBuilder();

    public Player(GamePanel gp) {
        this(gp, 0, 0);
    }

    public Player(GamePanel gp, double x, double y) {
        super(gp, x, y, GamePanel.TILE_SIZE, GamePanel.TILE_SIZE);
    }

    public void damage(int i) {
        health -= i;

        if (health <= 0)
            health = 0;
    }

    public int getTileX() {
        return (int) (x / GamePanel.TILE_SIZE);
    }

    public int getTileY() {
        return (int) (y / GamePanel.TILE_SIZE);
    }

    @Override
    public void render(GraphicsContext gc) {

        sb.setLength(0);



        gc.drawImage();
    }

    @Override
    public void update() {
//        lastX = x;
//        lastY = y;
//
        int dx = 0;
        int dy = 0;
//
//        if (keyH.wPressed) dy--;
//        if (keyH.sPressed) dy++;
//        if (keyH.aPressed) dx--;
//        if (keyH.dPressed) dx++;

        isMoving = dy != 0 || dx != 0;

        if (dx < 0) direction = "left";
        else if (dx > 0) direction = "right";
        else if (dy < 0) direction = "up";
        else if (dy > 0) direction = "down";

        // Reset flags and check collisions
        collisionUp = false;
        collisionDown = false;
        collisionLeft = false;
        collisionRight = false;

        // checks and handles collision
        gp.cChecker.checkOutOfBound(this, dx, dy, dt);
        gp.cChecker.checkObject(this, dx, dy, dt);
        gp.cChecker.checkTile(this, dx, dy, dt);

        // checks and handles interactions
        gp.interactionChecker.checkPickup(this);
        gp.interactionChecker.checkOpenContainer(this);

        // movement logic
        double currentSpeed = (dx != 0 && dy != 0) ? speedDiagonal : speed;
        if (dy < 0 && !collisionUp)    worldY -= currentSpeed * dt;
        if (dy > 0 && !collisionDown)  worldY += currentSpeed * dt;
        if (dx < 0 && !collisionLeft)  worldX -= currentSpeed * dt;
        if (dx > 0 && !collisionRight) worldX += currentSpeed * dt;

        gp.interactionChecker.checkStepping(this);

        spriteCounter += dt;
        final double animationTimerSc = 0.15; // change texture every 0.15 seconds
        if (spriteCounter > animationTimerSc) {
            spriteNum = (spriteNum % 4) + 1; // cycle 1-4
            spriteCounter = 0;
        }
    }

    @Override
    public double getDepth() {
        return 0;
    }
}
