package io.github.maze.maze;

import io.github.maze.entities.projectile.Meteor;
import io.github.maze.entities.Player;
import io.github.maze.game.GamePanel;
import io.github.maze.obstacles.*;
import io.github.maze.particle.MeteorImpactFrame;
import io.github.maze.particle.Thunder;

import java.util.ArrayList;
import java.util.List;

public class Maze {

    final GamePanel gp;
    public List<GameObject> objectList = new ArrayList<>();
    public GameObject[][] obstacleMap = new GameObject[GamePanel.ROW_HEIGHT][GamePanel.COL_WIDTH];

    public Player player;

    public int flagCount = 0;

    public Maze(GamePanel gp) {
        this.gp = gp;
    }

    public void addThunder(double centerX, double bottomY) {
        Thunder thunder = new Thunder(gp, centerX, bottomY);
        objectList.add(thunder);
    }

    public void addMeteorImpactFrame(double centerX, double centerY) {
        MeteorImpactFrame mif = new MeteorImpactFrame(gp, centerX, centerY);
        objectList.add(mif);
    }

    public void addMeteor(double targetX, double targetY) {
        Meteor meteor = new Meteor(gp, targetX, targetY);
        objectList.add(meteor);
    }

    public void addObject(int id, int col, int row) {

        if (col < 0 || col >= GamePanel.COL_WIDTH || row < 0 || row >= GamePanel.COL_WIDTH) {
            throw new IndexOutOfBoundsException("col: " + col + ", row: " + row);
        }

        double x = col * GamePanel.TILE_SIZE;
        double y = row * GamePanel.TILE_SIZE;

        /*
         * ID List
         *
         * 0 = air
         * 1 = BushWall
         * 2 = Spike
         * 3 = Hole
         * 4 = Key
         * 5 = Player
         * 6 = Ninja
         * 7 = Fire
         * 8 = Wizard
         * 9 = FireMonster
         * 10 = Flag
         *
         */

        switch (id) {
            case 0 /* air */ -> { /* do nothing */ }
            case 1 /* BushWall */ -> {
                BushWall bushWall = new BushWall(gp, x, y);
                objectList.add(bushWall);
                obstacleMap[row][col] = bushWall;
            }
            case 2 /* Spike */ -> {
                Spike spike = new Spike(gp, x, y);
                objectList.add(spike);
                obstacleMap[row][col] = spike;
            }
            case 3 /* Hole */ -> {
                Hole hole = new Hole(gp, col, row);
                objectList.add(hole);
                obstacleMap[row][col] = hole;
            }
            case 4 /* Key */ -> {
                Key key = new Key(gp, col, row);
                objectList.add(key);
                obstacleMap[row][col] = key;
            }
            case 5 /* Player */ -> {
                Player p = new Player(gp, x, y);
                objectList.add(p);

                if (this == gp.maze) {
                    gp.maze.player = p;
                }
            }
            case 6 /* Ninja */ -> {
                Ninja ninja = new Ninja(gp, x, y);
                objectList.add(ninja);
                obstacleMap[row][col] = ninja;
            }
            case 7 /* Fire */ -> {
                Fire fire = new Fire(gp, x, y);
                objectList.add(fire);
                obstacleMap[row][col] = fire;
            }
            case 8 /* Wizard */ -> {
                Wizard wizard = new Wizard(gp, x, y);
                objectList.add(wizard);
                obstacleMap[row][col] = wizard;
            }
            case 9 /* FireMonster */ -> {
                FireMonster fireMonster = new FireMonster(gp, x, y);
                objectList.add(fireMonster);
                obstacleMap[row][col] = fireMonster;
            }
            case 10 /* Flag */ -> {
                FlagGreen flagGreen = new FlagGreen(gp, x, y);
                objectList.add(flagGreen);
                obstacleMap[row][col] = flagGreen;
            }
        }
    }

    public void copyFrom(Maze replacement) {
        objectList = replacement.objectList;
        obstacleMap = replacement.obstacleMap;
        player = replacement.player;
    }
}
