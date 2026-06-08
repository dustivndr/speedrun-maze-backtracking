package io.github.maze.entities;

import io.github.maze.game.Game;
import io.github.maze.game.GamePanel;
import io.github.maze.input.InputHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;

public class Player extends Entity {

    final InputHandler inpH;

    private int walkCount = 0;

    public double lastX, lastY;

    public int MAX_HP = 100;
    private int health = MAX_HP;

    public int keyCount = 0;
    public int flagCount = 0;

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

    private final double NORMAL_SPEED = 3;
    private double currentSpeed = NORMAL_SPEED;

    private int poisonLength = 0;

    private int speedLength = 0;
    private int tilesWalkedWithSpeed = 0;

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
    }

    public void damage(int hp) {
        health -= hp;

        if (health <= 0)
            health = 0;
    }

    public void heal(int hp) {
        health += hp;
        if (health >= MAX_HP) {
            health = MAX_HP;
        }
    }

    public int getHP() { return health; }

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

        gc.drawImage(playerAssets.getTexture(sb.toString()), gp.camera.getScreenX(x), gp.camera.getScreenY(y));

    }

    @Override
    public void update() {

        lastX = x;
        lastY = y;

        int prevCol = (int) (lastX / GamePanel.TILE_SIZE);
        int prevRow = (int) (lastY / GamePanel.TILE_SIZE);

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

        // reset flags and check collisions
        collisionUp = collisionDown = collisionLeft = collisionRight = false;
        checkOutOfBound(dx, dy);

        // movement logic
        if (dy < 0 && !collisionUp)    y -= currentSpeed;
        if (dy > 0 && !collisionDown)  y += currentSpeed;
        if (dx < 0 && !collisionLeft)  x -= currentSpeed;
        if (dx > 0 && !collisionRight) x += currentSpeed;

        boolean movedTile = getTileX() != prevCol || getTileY() != prevRow;
        if (movedTile) {


            if (speedLength > 0) {
                tilesWalkedWithSpeed++;

                if (tilesWalkedWithSpeed % 2 == 0) {
                    if (poisonLength > 0) {
                        damage(1);

                        poisonLength--;
                    }
                    walkCount++;
                }

                currentSpeed = NORMAL_SPEED * 2;
                speedLength--;

                if (speedLength <= 0) {
                    tilesWalkedWithSpeed = 0;
                }
            }
            else {

                if (poisonLength > 0) {
                    damage(1);

                    poisonLength--;
                }

                tilesWalkedWithSpeed = 0;
                walkCount++;
                currentSpeed = NORMAL_SPEED;
            }
        }

        long curr = System.currentTimeMillis();
        spriteCounter += curr - lastTime;
        lastTime = curr;
        final double animationTimerMs = 100; // change texture every 150 millisecond
        if (spriteCounter > animationTimerMs) {
            spriteNum = (spriteNum % 4) + 1; // cycle 1-4
            spriteCounter = 0;
        }

    }

    public void checkOutOfBound(int dx, int dy) {

        double newX = x;
        double newY = y;
        if (dx == 1) {
            newX += currentSpeed;
        } else if (dx == -1) {
            newX -= currentSpeed;
        }

        if (dy == 1) {
            newY += currentSpeed;
        } else if (dy == -1) {
            newY -= currentSpeed;
        }

        if (newX < 0) collisionLeft = true;
        if (newX + width >= GamePanel.WORLD_WIDTH) collisionRight = true;
        if (newY < 0) collisionUp = true;
        if (newY + height >= GamePanel.WORLD_HEIGHT) collisionDown = true;

    }

    @Override
    public double getDepth() {
        return y + height;
    }

    public int getWalkCount() { return walkCount; }

    public void setPoisonLength(int poisonLength) { this.poisonLength = poisonLength; }
    public void setSpeedLength(int speedLength) {
        this.speedLength = speedLength;
    }

}
