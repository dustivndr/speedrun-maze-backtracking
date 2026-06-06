package io.github.maze.entities;

import io.github.maze.game.GamePanel;
import io.github.maze.input.InputHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;

public class Player extends Entity {

    final InputHandler inpH;

    public int walkCount = 0;

    public static int animationCounter = 0;
    public double lastX, lastY;

    private int health;

    public int keyCount = 0;

    static final PlayerAssets playerAssets = new PlayerAssets();

    private String direction = "down";
    private long spriteCounter = 0;
    private int spriteNum = 1;
    private long lastTime;
    private boolean isMoving = false;

    private boolean collisionUp = false;
    private boolean collisionRight = false;
    private boolean collisionDown = false;
    private boolean collisionLeft = false;

    private double currentSpeed = 15;

    private static StringBuilder sb = new StringBuilder();

    public Player(GamePanel gp) {
        this(gp, 0, 0);
    }

    public Player(GamePanel gp, double x, double y) {
        super(gp, x, y, GamePanel.TILE_SIZE, GamePanel.TILE_SIZE);
        inpH = gp.inputHandler;
        lastX = this.x;
        lastY = this.y;
        lastTime = System.currentTimeMillis();

        if (animationCounter + 1 > 1) {
            throw new IllegalArgumentException("Cannot add more than one player.");
        }
        animationCounter++;
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

    @Override public boolean getCollision() { return true; }

    @Override
    public void render(GraphicsContext gc) {

        sb.setLength(0);

        sb.append(direction);
        if (isMoving) {
            sb.append("Walk").append(spriteNum);

        } else {
            sb.append("Stationary");
        }

        gc.drawImage(playerAssets.getTexture(sb.toString()), x, y);
    }

    @Override
    public void update() {
        lastX = x;
        lastY = y;

        int prevCol = getTileY();
        int prevRow = getTileY();

        int dx = 0;
        int dy = 0;

        if (inpH.isPressed(KeyCode.W)) dy--;
        if (inpH.isPressed(KeyCode.S)) dy++;
        if (inpH.isPressed(KeyCode.A)) dx--;
        if (inpH.isPressed(KeyCode.D)) dx++;

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
//        checkOutOfBound(dx, dy);
//        gp.cChecker.checkObject(this, dx, dy);
//        gp.cChecker.checkTile(this, dx, dy);
//
//        // checks and handles interactions
//        gp.interactionChecker.checkPickup(this);
//        gp.interactionChecker.checkOpenContainer(this);
//
//        // movement logic
//        double currentSpeed = (dx != 0 && dy != 0) ? speedDiagonal : speed;
        if (dy < 0 && !collisionUp)    y -= currentSpeed;
        if (dy > 0 && !collisionDown)  y += currentSpeed;
        if (dx < 0 && !collisionLeft)  x -= currentSpeed;
        if (dx > 0 && !collisionRight) x += currentSpeed;
//
//        gp.interactionChecker.checkStepping(this);
//

        if (getTileX() != prevCol || getTileY() != prevRow) {
            walkCount++;
        }

        long curr = System.currentTimeMillis();
        spriteCounter += lastTime - curr;
        lastTime = curr;
        final double animationTimerSc = 0.15; // change texture every 0.15 seconds
        if (spriteCounter > animationTimerSc) {
            spriteNum = (spriteNum % 4) + 1; // cycle 1-4
            spriteCounter = 0;
        }
    }

    public void checkOutOfBound(int dx, int dy) {

    }

    @Override
    public double getDepth() {
        return 0;
    }
}
