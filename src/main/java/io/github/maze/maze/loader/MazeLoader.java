package io.github.maze.maze.loader;

import io.github.maze.game.GamePanel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MazeLoader {

    final GamePanel gp;

    List<String> files;

    public MazeLoader(GamePanel gp) {
        this.gp = gp;

        files = new ArrayList<>();
        try {
            try (InputStream in = getClass().getResourceAsStream("/maps")) {
                BufferedReader br = new BufferedReader(new InputStreamReader(in));

                String path;
                while ((path = br.readLine()) != null) {
                    if (path.endsWith(".txt")) {
                        files.add(path);
                    }
                }
            }
        }

        catch (IOException e) {
            e.printStackTrace();
        }
    }
}