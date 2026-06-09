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

    /*
     * ID List
     *
     * 0 = air
     * B = BushWall (gk bs ditembus)
     * S = Spike (5 dmg)
     * H = Hole (awal no collision. wkt nyentuh 5 dmg trs dpt collision, gk bisa di jalani)
     * K = Key (perlu buat unlock flag locked)
     * P = Player
     * N = Ninja (5 dmg, 3x3 range)
     * R = Fire (5 dmg)
     * W = Wizard (summon lightning 10 dmg, 5x5 range)
     * M = FireMonster (jatuhin meteor 20 dmg, 6x5 range)
     * F = Flag Green (perlu ngambil semua spy menang)
     * f = Flag Red (flag palsu, buat algoritma asli tpi gk perlu spy menang)
     * L = Flag Locked (sm kek flag green tpi perlu kunci spy bs diambil)
     * s = Speed spell (speed buat 20 tile gerak)
     * p = Poison spell (1 dmg tiap tile gerak)
     * h = Heal spell (heal 20 hp)
     * E = Elf (heal ke full)
     * O = Portal (teleport ke koneksinya)
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
            case 'F' /* Flag Green */ -> {
                FlagGreen flagGreen = new FlagGreen(gp, x, y);
                objectList.add(flagGreen);
                obstacleMap[row][col] = flagGreen;
                flagCount++;
            }
            case 'f' /* Flag Red */ -> {
                FlagRed flagRed = new FlagRed(gp, x, y);
                objectList.add(flagRed);
                obstacleMap[row][col] = flagRed;
            }
            case 'L' /* Flag Locked */ -> {
                FlagLocked fl = new FlagLocked(gp, x, y);
                objectList.add(fl);
                obstacleMap[row][col] = fl;
                flagCount++;
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
}
