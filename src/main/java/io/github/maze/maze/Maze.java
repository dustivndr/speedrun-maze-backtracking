package io.github.maze.maze;

import io.github.maze.entities.Player;
import io.github.maze.game.GamePanel;
import io.github.maze.obstacles.*;

import java.util.ArrayList;
import java.util.List;

public class Maze {

    final GamePanel gp;
    public List<GameObject> objectList = new ArrayList<>();
    public GameObject[][] obstacleMap = new GameObject[GamePanel.COL_HEIGHT][GamePanel.ROW_WIDTH];

    public Maze(GamePanel gp) {
        this.gp = gp;

        // DEBUG
//        addObject(2, 1, 1);
    }

    public void addObject(int id, int col, int row) {

        double x = col * GamePanel.TILE_SIZE;
        double y = row * GamePanel.TILE_SIZE;

        /**
         * ID List
         *
         * 0 = air
         * 1 = BushWall
         * 2 = Spike
         * 3 = Hole
         * 4 = Key
         * 5 = Player
         *
         */

        switch (id) {
            case 0 /* air */ -> { /* do nothing */ }
            case 1 /* BushWall */ -> {
                BushWall bushWall = new BushWall(gp, x, y);
                objectList.add(bushWall);
                obstacleMap[col][row] = bushWall;
            }
            case 2 /* Spike */ -> {
                Spike spike = new Spike(gp, x, y);
                objectList.add(spike);
                obstacleMap[col][row] = spike;
            }
            case 3 /* Hole */ -> {
                Hole hole = new Hole(gp, col, row);
                objectList.add(hole);
                obstacleMap[col][row] = hole;
            }
            case 4 /* Key */ -> {
                Key key = new Key(gp, col, row);
                objectList.add(key);
                obstacleMap[col][row] = key;
            }
            case 5 /* Player */ -> {
                Player p = new Player(gp, x, y);
                objectList.add(p);
                gp.player = p;
            }
            case 6 /* Ninja */ -> {
                Ninja ninja = new Ninja(gp, x, y);
                objectList.add(ninja);
                obstacleMap[col][row] = ninja;
            }
        }
    }
}
