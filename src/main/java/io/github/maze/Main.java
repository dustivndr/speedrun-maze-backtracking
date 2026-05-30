package io.github.maze;

/*
* THIS PROJECT CREATED WITH JETBRAINS 25.0.3
* JAVA VERSION LOWER THAN 25 WON'T WORK !!
*
*/

import io.github.maze.game.Game;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {

        Game game = new Game();

        Scene scene = new Scene(game);

        stage.setTitle("Maze");
        stage.setScene(scene);
        stage.show();

        game.showMenu();

    }

    public static void main(String[] args) {
        launch(args);
    }
}