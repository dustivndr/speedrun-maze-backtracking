package io.github.maze;

/*
* REQUIRES JAVA 25+
*
*/

import java.util.Objects;

import io.github.maze.game.Game;
import io.github.maze.util.Fonts;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static final class Constants {

        public static final int WINDOW_WIDTH = 800;
        public static final int WINDOW_HEIGHT = 600;

        private Constants() {}
    }

    @Override
    public void start(Stage stage) {

        Fonts.initialize();

        Game game = new Game();

        Scene scene = new Scene(
                game,
                Constants.WINDOW_WIDTH,
                Constants.WINDOW_HEIGHT
        );

        scene.getStylesheets().add(
                Objects.requireNonNull(
                        getClass().getResource("/styles/menu.css")
                ).toExternalForm()
        );

        stage.setTitle("Maze");
        stage.setScene(scene);

        stage.setResizable(false);

        game.showMenu();

        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}