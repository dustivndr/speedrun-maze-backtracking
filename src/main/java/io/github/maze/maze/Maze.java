package io.github.maze.maze;

import io.github.maze.game.GamePanel;
import io.github.maze.obstacles.BushWall;
import io.github.maze.obstacles.Hole;
import io.github.maze.obstacles.Key;
import io.github.maze.obstacles.Spike;

import java.util.ArrayList;
import java.util.List;

public class Maze {

    final GamePanel gp;
    public List<GameObject> objectList = new ArrayList<>();

    public Maze(GamePanel gp) {
        this.gp = gp;

        // DEBUG
//        addObject(2, 1, 1);
    }

    public void addObject(int id, int col, int row) {

        double x = col * GamePanel.TILE_SIZE * GamePanel.SCALE;
        double y = row * GamePanel.TILE_SIZE * GamePanel.SCALE;

        /**
         * id list:
         *
         * 0: air
         * 1: BushWall
         * 2: Spike
         * 3: Hole
         * 4: Key
         */

        switch (id) {
            // air, do not add GameObject
            case 0 -> {}
            case 1 -> { objectList.add(new BushWall(gp, x, y)); }   // BushWall
            case 2 -> { objectList.add(new Spike(gp, x, y)); }      // Spike

            // Hole
            case 3 -> {
                // TODO: commented since Hole constructor hasn't yet been made
//                objectList.add(new Hole(gp, x, y));
            }

            case 4 -> { objectList.add(new Key(gp, col, row)); }    // Key
        }
    }
}
