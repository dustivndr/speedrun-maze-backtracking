package io.github.maze.obstacles;

import io.github.maze.game.Game;
import io.github.maze.game.GamePanel;
import io.github.maze.maze.GameObject;
import io.github.maze.util.Util;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class BushWall extends Obstacle {

    public static final BushWallAssets frames = new BushWallAssets();

    static final StringBuilder sb = new StringBuilder(8);

    public Image cachedFrame;

    public BushWall(GamePanel gp, double x, double y) {
        super(gp, x, y, 1, 1.5);
    }

    @Override public double getDepth() { return y + GamePanel.TILE_SIZE; }
    @Override public boolean getCollision() { return true; }

    @Override
    public void render(GraphicsContext gc) {


        if (cachedFrame == null) {

            int row = (int) (y / GamePanel.TILE_SIZE);
            int col = (int) (x / GamePanel.TILE_SIZE);

            sb.setLength(0);
            getBushWallConnections();
            if (sb.toString().equals("leftright") || sb.toString().equals("updown")) {
                sb.append(Util.rand.nextInt(1, 3));
            }

            cachedFrame = frames.getTexture(sb.toString());
        }

        gc.drawImage(cachedFrame, x, y - GamePanel.TILE_SIZE);
    }

    private void getBushWallConnections() {
        int col = (int) (x / GamePanel.TILE_SIZE);
        int row = (int) (y / GamePanel.TILE_SIZE);

        boolean found = false;

        for (int i = 0; i < gp.maze.objectList.size(); i++) {
            GameObject o = gp.maze.objectList.get(i);

            int objCol = (int) (o.getX() / GamePanel.TILE_SIZE);
            int objRow = (int) (o.getY() / GamePanel.TILE_SIZE);

            // check up
            if (objCol == col && objRow == row - 1) {
                sb.append("up");
                found = true;
            }

            // check down
            if (objCol == col && objRow == row + 1) {
                sb.append("down");
                found = true;
            }

            // check left
            if (objCol - 1 == col && objRow == row) {
                sb.append("left");
                found = true;
            }

            // check right
            if (objCol + 1 == col && objRow == row) {
                sb.append("right");
                found = true;
            }
        }

        if (!found) {
            sb.append("single");
        }
    }

    @Override
    public void update() {
        // BushWall needs no updating
    }
}
