package io.github.maze.obstacles;

import io.github.maze.game.GamePanel;
import io.github.maze.maze.GameObject;
import io.github.maze.render.Camera;
import io.github.maze.util.Util;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class BushWall extends Obstacle {

    public static final BushWallAssets frames = new BushWallAssets();

    static final StringBuilder sb = new StringBuilder(8);

    public Image cachedFrame;
    final Camera camera;

    public BushWall(GamePanel gp, double x, double y) {
        super(gp, x, y, GamePanel.TILE_SIZE, GamePanel.TILE_SIZE);
        camera = gp.camera;
    }

    @Override public double getDepth() { return y + GamePanel.TILE_SIZE; }
    @Override public boolean getCollision() { return true; }

    @Override
    public void render(GraphicsContext gc) {

        if (cachedFrame == null) {

            sb.setLength(0);
            getBushWallConnections();
            if (sb.toString().equals("leftright") || sb.toString().equals("updown")) {
                sb.append(Util.rand.nextInt(1, 3));
            }

            cachedFrame = frames.getTexture(sb.toString());
        }

        gc.drawImage(cachedFrame, camera.getScreenX(x), camera.getScreenY(y - GamePanel.TILE_SIZE));
    }

    private void getBushWallConnections() {
        int col = (int) (x / GamePanel.TILE_SIZE);
        int row = (int) (y / GamePanel.TILE_SIZE);

        boolean found = false;

        boolean up = false;
        boolean down = false;
        boolean left = false;
        boolean right = false;

        for (int i = 0; i < gp.maze.objectList.size(); i++) {
            GameObject o = gp.maze.objectList.get(i);

            if (!(o instanceof BushWall)) continue;

            int objCol = (int) (o.getX() / GamePanel.TILE_SIZE);
            int objRow = (int) (o.getY() / GamePanel.TILE_SIZE);

            // check up
            if (objCol == col && objRow == row - 1) {
                up = true;
                found = true;
            }

            // check down
            if (objCol == col && objRow == row + 1) {
                down = true;
                found = true;
            }

            // check left
            if (objCol == col - 1 && objRow == row) {
                left = true;
                found = true;
            }

            // check right
            if (objCol == col + 1 && objRow == row) {
                right = true;
                found = true;
            }
        }

        if (up) sb.append("up");
        if (down) sb.append("down");
        if (left) sb.append("left");
        if (right) sb.append("right");

        if (!found) {
            sb.append("single");
        }
    }

    @Override
    public void update() {
        // BushWall needs no updating
    }
}
