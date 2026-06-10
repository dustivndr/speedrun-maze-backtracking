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

    public String[][] strMap = new String[GamePanel.ROW_HEIGHT][GamePanel.COL_WIDTH];

    public Player player;

    public int flagCount = 0;

    /*
     * ID List
     *
     * 0 = air
     * B = BushWall (has collision)
     * S = Spike
     *        - 5 dmg
     *        - no collision
     * H = Hole (
     *        - before touched by player: has no collision
     *        - when touched by player  : deals 5 dmg
     *        - after touched by player : has collision)
     * K = Key
     *        - once touched, player keyCount++
     *        - touched key is removed
     *        - needed to unlock FlagLocked
     *        - once used to unlock a flag, key is consumed
     *        - no collision
     * P = Player
     *        - win con     : takes every green and locked flag
     *        - optimal path: win con achieved with most hp left
     *        - lose con    : hp = 0
     * N = Ninja
     *        - 5 dmg,
     *        - 3x3 range,
     *        - attacks on initial entry into range (0)
     *        - attacks every 2 tiles walked subsequently (2, 4, 6...)
     *          as long as player is still inside the range
     *        - has collision
     * R = Fire
     *        - 5 dmg
     *        - no collision
     * W = Wizard
     *        - 10 dmg,
     *        - 5x5 range
     *        - attacks on initial player entry (0)
     *        - attacks every 4 tiles walked subsequently (4, 8, 12...)
     *          as long as player is still inside range
     *        - has collision
     * M = FireMonster
     *        - 20 dmg,
     *        - 6x5 range,
     *        - attacks immediately when the player enters is range
     *          then stops until the player reenters
     *        - has collision
     *        - collision size: 2x1 (2 col, 1 row)
     * F = Flag Green
     *        - perlu ngambil semua spy menang
     *        - no collision
     * f = Flag Red
     *        - the backtracking algorithm thinks it's a green flag, but it isn't needed for the win con
     *        - no collision
     * L = Flag Locked
     *        - same as green flag
     *        - needs key to be collected
     *        - no collision
     * s = Speed spell
     *        - 2x speed buat 20 tile gerak
     *        - tile steps increments every 2 tiles walked
     *              (changes from the usual of every tile;
     *              this matters for ninja, wizard, and poison spell)
     * p = Poison spell
     *        - 1 dmg every tile count
     *        - lasts 10 tiles
     *        - if takes multiple poison, dmg doesn't stack.
     *          only the poisonLength is back to 10
     * h = Heal spell
     *        - heal 20 HP
     * E = Elf
     *        - heal to FULL HP
     *        - has collision
     *        - 3x3 range
     * O = Portal
     *        - if touched, teleport to the connected portal's
     *          coordinate (O1-O1 | O2-O2 | O3-O3)
     *        - no collision
     *
     */

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

        switch (id) {
            case '0' /* air */ -> {
                strMap[row][col] = "0";
            }
            case 'B' /* BushWall */ -> {
                BushWall bushWall = new BushWall(gp, x, y);
                objectList.add(bushWall);
                obstacleMap[row][col] = bushWall;
                strMap[row][col] = "B";
            }
            case 'S' /* Spike */ -> {
                Spike spike = new Spike(gp, x, y);
                objectList.add(spike);
                obstacleMap[row][col] = spike;
                strMap[row][col] = "S";
            }
            case 'H' /* Hole */ -> {
                Hole hole = new Hole(gp, col, row);
                objectList.add(hole);
                obstacleMap[row][col] = hole;
                strMap[row][col] = "H";
            }
            case 'K' /* Key */ -> {
                Key key = new Key(gp, col, row);
                objectList.add(key);
                obstacleMap[row][col] = key;
                strMap[row][col] = "K";
            }
            case 'P' /* Player */ -> {
                Player p = new Player(gp, x, y);
                objectList.add(p);

                if (this == gp.maze) {
                    gp.maze.player = p;
                }

                strMap[row][col] = "0";
            }
            case 'N' /* Ninja */ -> {
                Ninja ninja = new Ninja(gp, x, y);
                objectList.add(ninja);
                obstacleMap[row][col] = ninja;
                strMap[row][col] = "N";
            }
            case 'R' /* Fire */ -> {
                Fire fire = new Fire(gp, x, y);
                objectList.add(fire);
                obstacleMap[row][col] = fire;
                strMap[row][col] = "R";
            }
            case 'W' /* Wizard */ -> {
                Wizard wizard = new Wizard(gp, x, y);
                objectList.add(wizard);
                obstacleMap[row][col] = wizard;
                strMap[row][col] = "W";
            }
            case 'M' /* FireMonster */ -> {
                FireMonster fireMonster = new FireMonster(gp, x, y);
                objectList.add(fireMonster);
                obstacleMap[row][col] = fireMonster;
                strMap[row][col] = "M";
            }
            case 'F' /* Flag Green */ -> {
                FlagGreen flagGreen = new FlagGreen(gp, x, y);
                objectList.add(flagGreen);
                obstacleMap[row][col] = flagGreen;
                flagCount++;
                strMap[row][col] = "F";
            }
            case 'f' /* Flag Red */ -> {
                FlagRed flagRed = new FlagRed(gp, x, y);
                objectList.add(flagRed);
                obstacleMap[row][col] = flagRed;
                strMap[row][col] = "f";
            }
            case 'L' /* Flag Locked */ -> {
                FlagLocked fl = new FlagLocked(gp, x, y);
                objectList.add(fl);
                obstacleMap[row][col] = fl;
                flagCount++;
                strMap[row][col] = "L";
            }
            case 's' /* Speed Spell */ -> {
                SpeedSpell s = new SpeedSpell(gp, x, y);
                objectList.add(s);
                obstacleMap[col][row] = s;
                strMap[row][col] = "s";
            }
            case 'p' /* Poison Spell */ -> {
                PoisonSpell s = new PoisonSpell(gp, x, y);
                objectList.add(s);
                obstacleMap[col][row] = s;
                strMap[row][col] = "p";
            }
            case 'h' /* Heal Spell */ -> {
                HealSpell s = new HealSpell(gp, x, y);
                objectList.add(s);
                obstacleMap[col][row] = s;
                strMap[row][col] = "h";
            }
            case 'E' /* Elf */ -> {
                Elf e = new Elf(gp, x, y);
                objectList.add(e);
                obstacleMap[col][row] = e;
                strMap[row][col] = "E";
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
        strMap[row][col] = "O" + num;

        return p;
    }

    public void copyFrom(Maze replacement) {
        objectList = replacement.objectList;
        obstacleMap = replacement.obstacleMap;
        player = replacement.player;
    }

    public String[][] getStrMap() {
        return strMap;
    }

   public char[][] toCharMap() {

    char[][] map =
            new char[GamePanel.ROW_HEIGHT]
                    [GamePanel.COL_WIDTH];

    for (int r = 0; r < GamePanel.ROW_HEIGHT; r++) {

        for (int c = 0; c < GamePanel.COL_WIDTH; c++) {

            GameObject obj = obstacleMap[r][c];

            if (obj == null) {
                map[r][c] = '0';
            }
            else if (obj instanceof BushWall) {
                map[r][c] = 'B';
            }
            else if (obj instanceof Spike) {
                map[r][c] = 'S';
            }
            else if (obj instanceof Hole) {
                map[r][c] = 'H';
            }
            else if (obj instanceof Key) {
                map[r][c] = 'K';
            }
            else if (obj instanceof Ninja) {
                map[r][c] = 'N';
            }
            else if (obj instanceof Fire) {
                map[r][c] = 'R';
            }
            else if (obj instanceof Wizard) {
                map[r][c] = 'W';
            }
            else if (obj instanceof FireMonster) {
                map[r][c] = 'M';
            }
            else if (obj instanceof FlagGreen) {
                map[r][c] = 'F';
            }
            else if (obj instanceof FlagLocked) {
                map[r][c] = 'L';
            }
            else if (obj instanceof FlagRed) {
                map[r][c] = 'f';
            }
            else if (obj instanceof SpeedSpell) {
                map[r][c] = 's';
            }
            else if (obj instanceof PoisonSpell) {
                map[r][c] = 'p';
            }
            else if (obj instanceof HealSpell) {
                map[r][c] = 'h';
            }
            else if (obj instanceof Elf) {
                map[r][c] = 'E';
            }
            else {
                map[r][c] = '0';
            }
        }
    }

    return map;
}
}
