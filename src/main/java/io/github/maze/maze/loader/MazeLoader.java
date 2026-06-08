package io.github.maze.maze.loader;

/*
 * Load Map from TXT (~/maze/maps/mapX.txt)
 *
*/

import io.github.maze.entities.Player;
import io.github.maze.game.GamePanel;
import io.github.maze.maze.Maze;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MazeLoader {

    final GamePanel gp;

    List<String> files;
    public int currentFile = 0;

    public MazeLoader(GamePanel gp) {
        this.gp = gp;

        files = new ArrayList<>();
        int index = 0;
        while (true) {
            String path = "/maps/map" + index + ".txt";
            if (getClass().getResource(path) != null) {
                files.add(path);
                index++;
            } else {
                break;
            }
        }

        if (files.isEmpty()) {
            throw new IllegalStateException("No map files found/defined!");
        }

    }

    public void loadNextMapObstacles() {
        loadObstacles(files.get(currentFile), gp.maze);
        currentFile = (currentFile + 1) % files.size();
    }

    public void loadNextMapPlayer() {
        loadPlayer(files.get(currentFile), gp.maze);
        currentFile = (currentFile + 1) % files.size();
    }

    public void loadObstacles(String mapPath, Maze maze) {

        try (InputStream is = getClass().getResourceAsStream(mapPath)) {

            if (is == null) {
                return;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            int row = 0;

            while ((line = br.readLine()) != null) {

                String[] str = line.split(" ");

                for (int col = 0; col < str.length; col++) {

                    if (!str[col].isEmpty()) {
                        char id = str[col].charAt(0);

                        if (id == 'F') {
                            maze.flagCount++;
                        }

                        // ignore player
                        if (id != 'P') {
                            maze.addObject(id, col, row);
                        }
                    }
                }

                row++;
            }

            br.close();

        } catch (IOException | NumberFormatException e) {
            System.err.println("Error parsing map file: " + mapPath);
            e.printStackTrace();
        }
    }

    public void loadPlayer(String mapPath, Maze maze) {

        try (InputStream is = getClass().getResourceAsStream(mapPath);
             BufferedReader br = (is != null) ? new BufferedReader(new InputStreamReader(is)) : null) {

            if (is == null) return;

            String line;
            int row = 0;

            while ((line = br.readLine()) != null) {
                String[] str = line.split(" ");
                for (int col = 0; col < str.length; col++) {
                    if (!str[col].isEmpty()) {
                        int id = str[col].charAt(0);

                        if (id == 'P') {
                            double pixelX = col * GamePanel.TILE_SIZE;
                            double pixelY = row * GamePanel.TILE_SIZE;

                            // add a new player to the maze instance
                            Player p = new Player(gp, pixelX, pixelY);
                            maze.player = p;
                            maze.objectList.add(p);

                            return;
                        }
                    }
                }
                row++;
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error parsing player in map file: " + mapPath);
            e.printStackTrace();
        }
    }
}
