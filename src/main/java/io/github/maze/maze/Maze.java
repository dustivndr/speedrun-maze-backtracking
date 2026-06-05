package io.github.maze.maze;

import io.github.maze.entities.Player;
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

        double x = col * GamePanel.TILE_SIZE;
        double y = row * GamePanel.TILE_SIZE;

        switch (id) {
            case 0 /* air */ -> { /* do nothing */ }
            case 1 /* BushWall */ -> { objectList.add(new BushWall(gp, x, y)); }
            case 2 /* Spike */ -> { objectList.add(new Spike(gp, x, y)); }
            case 3 /* Hole */ -> { objectList.add(new Hole(gp, col, row)); }
            case 4 /* Key */ -> { objectList.add(new Key(gp, col, row)); }
            case 5 /* Player */ -> {
                Player p = new Player(gp, x, y);
                objectList.add(p);
                gp.player = p;
            }
        }
    }
}
