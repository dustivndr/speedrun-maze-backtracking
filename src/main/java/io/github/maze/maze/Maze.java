package io.github.maze.maze;

import io.github.maze.entities.projectile.Meteor;
import io.github.maze.entities.Player;
import io.github.maze.game.GamePanel;
import io.github.maze.obstacles.*;
import io.github.maze.particle.MeteorImpactFrame;
import io.github.maze.particle.Thunder;

import java.util.ArrayList;
import java.util.List;
import io.github.maze.obstacles.*;

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

    public void addObject(char id, int col, int row) {

        if (col < 0 || col >= GamePanel.COL_WIDTH || row < 0 || row >= GamePanel.COL_WIDTH) {
            throw new IndexOutOfBoundsException("col: " + col + ", row: " + row);
        }

        double x = col * GamePanel.TILE_SIZE;
        double y = row * GamePanel.TILE_SIZE;

        /*
         * ID List
         *
         * 0 = air
         * B = BushWall
         * S = Spike
         * H = Hole
         * K = Key
         * P = Player
         * N = Ninja
         * R = Fire
         * W = Wizard
         * M = FireMonster
         * F = Flag
         * s = Speed spell
         * p = Poison spell
         * h = Heal spell
         * E = Elf
         *
         */

        switch (id) {
            case '0' /* air */ -> { /* do nothing */ }
            case 'B' /* BushWall */ -> {
                BushWall bushWall = new BushWall(gp, x, y);
                objectList.add(bushWall);
                obstacleMap[row][col] = bushWall;
            }
            case 'S' /* Spike */ -> {
                Spike spike = new Spike(gp, x, y);
                objectList.add(spike);
                obstacleMap[row][col] = spike;
            }
            case 'H' /* Hole */ -> {
                Hole hole = new Hole(gp, col, row);
                objectList.add(hole);
                obstacleMap[row][col] = hole;
            }
            case 'K' /* Key */ -> {
                Key key = new Key(gp, col, row);
                objectList.add(key);
                obstacleMap[row][col] = key;
            }
            case 'P' /* Player */ -> {
                Player p = new Player(gp, x, y);
                objectList.add(p);

                if (this == gp.maze) {
                    gp.maze.player = p;
                }
            }
            case 'N' /* Ninja */ -> {
                Ninja ninja = new Ninja(gp, x, y);
                objectList.add(ninja);
                obstacleMap[row][col] = ninja;
            }
            case 'R' /* Fire */ -> {
                Fire fire = new Fire(gp, x, y);
                objectList.add(fire);
                obstacleMap[row][col] = fire;
            }
            case 'W' /* Wizard */ -> {
                Wizard wizard = new Wizard(gp, x, y);
                objectList.add(wizard);
                obstacleMap[row][col] = wizard;
            }
            case 'M' /* FireMonster */ -> {
                FireMonster fireMonster = new FireMonster(gp, x, y);
                objectList.add(fireMonster);
                obstacleMap[row][col] = fireMonster;
            }
            case 'F' /* Flag */ -> {
                FlagGreen flagGreen = new FlagGreen(gp, x, y);
                objectList.add(flagGreen);
                obstacleMap[row][col] = flagGreen;
            }
            case 's' /* Speed Spell */ -> {
                SpeedSpell s = new SpeedSpell(gp, x, y);
                objectList.add(s);
                obstacleMap[col][row] = s;
            }
            case 'p' /* Poison Spell */ -> {
                PoisonSpell s = new PoisonSpell(gp, x, y);
                objectList.add(s);
                obstacleMap[col][row] = s;
            }
            case 'h' /* Heal Spell */ -> {
                HealSpell s = new HealSpell(gp, x, y);
                objectList.add(s);
                obstacleMap[col][row] = s;
            }
            case 'E' /* Elf */ -> {
                Elf e = new Elf(gp, x, y);
                objectList.add(e);
                obstacleMap[col][row] = e;
            }
        }
    }

    public Portal addPortal(int col, int row, int num) {
        if (col < 0 || col >= GamePanel.COL_WIDTH || row < 0 || row >= GamePanel.COL_WIDTH) {
            throw new IndexOutOfBoundsException("col: " + col + ", row: " + row);
        }

        double x = col * GamePanel.TILE_SIZE;
        double y = row * GamePanel.TILE_SIZE;

        Portal p = new Portal(gp, x, y, num);
        objectList.add(p);
        obstacleMap[col][row] = p;

        return p;
    }

    public void copyFrom(Maze replacement) {
        objectList = replacement.objectList;
        obstacleMap = replacement.obstacleMap;
        player = replacement.player;
    }

    public char[][] toCharMap() {

    char[][] map =
        new char[GamePanel.ROW_HEIGHT]
                [GamePanel.COL_WIDTH];

    for(int r = 0; r < GamePanel.ROW_HEIGHT; r++) {

        for(int c = 0; c < GamePanel.COL_WIDTH; c++) {

            GameObject obj =
                    obstacleMap[r][c];

            if(obj == null) {
                map[r][c] = '0';
            }
            else if(obj instanceof BushWall) {
                map[r][c] = 'B';
            }
            else if(obj instanceof Spike) {
                map[r][c] = 'S';
            }
            else if(obj instanceof Hole) {
                map[r][c] = 'H';
            }
            else if(obj instanceof FlagGreen) {
                map[r][c] = 'F';
            }
            else {
                map[r][c] = '0';
            }
        }
    }

    return map;
}
}
