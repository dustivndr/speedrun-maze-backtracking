package io.github.maze.entities;

import io.github.maze.game.GamePanel;
import io.github.maze.input.InputHandler;
import io.github.maze.maze.GameObject;
import io.github.maze.obstacles.BushWall;
import io.github.maze.obstacles.Elf;
import io.github.maze.obstacles.FireMonster;
import io.github.maze.obstacles.Hole;
import io.github.maze.obstacles.Ninja;
import io.github.maze.obstacles.Wizard;
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

    private final double NORMAL_SPEED = 5;
    public double currentSpeed = NORMAL_SPEED;

    private int fireLength = 0;

    private int speedLength = 0;
    private int tilesWalkedWithSpeed = 0;

    private static StringBuilder sb = new StringBuilder();

    // AUTO PLAYER
    private int autoDx = 0;
    private int autoDy = 0;
    private boolean autoMode = false;

    public void setAutoDirection(int dx, int dy) {
        autoDx = dx;
        autoDy = dy;
    }

    public void setAutoMode(boolean autoMode) {
        this.autoMode = autoMode;
    }

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
        if (health <= 0) health = 0;
    }

    public void heal(int hp) {
        health += hp;
        if (health >= MAX_HP) health = MAX_HP;
    }

    public void moveUp() { y -= currentSpeed; }
    public void moveDown() { y += currentSpeed; }
    public void moveLeft() { x -= currentSpeed; }
    public void moveRight() { x += currentSpeed; }

    public int getHP() { return health; }

    public int getTileX() {
        return (int) (x / GamePanel.TILE_SIZE);
    }

    public int getTileY() {
        return (int) (y / GamePanel.TILE_SIZE);
    }

    public void snapToTile() {
        x = getTileX() * GamePanel.TILE_SIZE;
        y = getTileY() * GamePanel.TILE_SIZE;
    }

    @Override
    public boolean getCollision() { return true; }

    @Override
    public void render(GraphicsContext gc) {

        sb.setLength(0);

        sb.append(direction);
        if (isMoving) {
            sb.append("Walk").append(spriteNum);
        } else {
            sb.append("Stationary");
        }

        gc.drawImage(
            playerAssets.getTexture(sb.toString()),
            gp.camera.getScreenX(x),
            gp.camera.getScreenY(y)
        );
    }

    @Override
    public void update() {

        lastX = x;
        lastY = y;

        int prevCol = (int) (lastX / GamePanel.TILE_SIZE);
        int prevRow = (int) (lastY / GamePanel.TILE_SIZE);

        int dx = 0;
        int dy = 0;

        // prioritas auto player
        if (autoMode) {
            dx = autoDx;
            dy = autoDy;
        } else {
            if (inpH.isPressed(KeyCode.W)) dy--;
            if (inpH.isPressed(KeyCode.S)) dy++;
            if (inpH.isPressed(KeyCode.A)) dx--;
            if (inpH.isPressed(KeyCode.D)) dx++;
        }

        isMoving = dy != 0 || dx != 0;

        if (dx < 0) direction = "left";
        else if (dx > 0) direction = "right";
        else if (dy < 0) direction = "up";
        else if (dy > 0) direction = "down";

        double moveX = dx * currentSpeed;
        double moveY = dy * currentSpeed;

//        if (canOccupy(x + moveX, y + moveY)) {
//            x += moveX;
//            y += moveY;
//        } else {
//            if (moveY != 0 && canOccupy(x, y + moveY)) {
//                y += moveY;
//            }
//
//            if (moveX != 0 && canOccupy(x + moveX, y)) {
//                x += moveX;
//            }
//        }

        y += moveY;
        x += moveX;

        boolean movedTile = getTileX() != prevCol || getTileY() != prevRow;

        if (movedTile) {

            if (speedLength > 0) {

                tilesWalkedWithSpeed++;

                if (tilesWalkedWithSpeed % 2 == 0) {

                    if (fireLength > 0) {
                        damage(1);
                        fireLength--;
                    }

                    walkCount++;
                }

                currentSpeed = NORMAL_SPEED * 2;
                speedLength--;

                if (speedLength <= 0) {
                    tilesWalkedWithSpeed = 0;
                }

            } else {

                if (fireLength > 0) {
                    damage(1);
                    fireLength--;
                }

                tilesWalkedWithSpeed = 0;
                walkCount++;
                currentSpeed = NORMAL_SPEED;
            }
        }

        long curr = System.currentTimeMillis();
        spriteCounter += curr - lastTime;
        lastTime = curr;

        if (spriteCounter > 100) {
            spriteNum = (spriteNum % 4) + 1;
            spriteCounter = 0;
        }

//        SoundManager.FOOTSTEP_SFX.play();
    }

    private boolean canOccupy(double nextX, double nextY) {

        if (nextX < 0 || nextX + width > GamePanel.WORLD_WIDTH) {
            return false;
        }

        if (nextY < 0 || nextY + height > GamePanel.WORLD_HEIGHT) {
            return false;
        }

        int nextCol = (int) (nextX / GamePanel.TILE_SIZE);
        int nextRow = (int) (nextY / GamePanel.TILE_SIZE);

        if (nextRow < 0 || nextRow >= GamePanel.ROW_HEIGHT || nextCol < 0 || nextCol >= GamePanel.COL_WIDTH) {
            return false;
        }

        GameObject obj = gp.maze.obstacleMap[nextRow][nextCol];

        if (obj instanceof BushWall ||
                obj instanceof Ninja ||
                obj instanceof Wizard ||
                obj instanceof FireMonster ||
                obj instanceof Elf) {
            return false;
        }

        if (obj instanceof Hole) {
            return false;
        }

        int leftCol = nextCol - 1;
        if (leftCol >= 0 && gp.maze.obstacleMap[nextRow][leftCol] instanceof FireMonster) {
            return false;
        }

        return true;
    }

    @Override
    public double getDepth() {
        return y + height;
    }

    public int getWalkCount() { return walkCount; }

    public void setFireLength(int fireLength) {
        this.fireLength = fireLength;
    }

    public void setSpeedLength(int speedLength) {
        this.speedLength = speedLength;
    }

    
}