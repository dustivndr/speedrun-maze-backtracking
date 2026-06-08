package io.github.maze.audio;

/*
 * CONTROL BACKGROUND MUSIC IN THE GAME
 *
 * example on how to use in game:
 * MusicManager.playMenuMusic();
 *
 */

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MusicManager {

    public static void playMenuMusic() {

//        Media media = new Media(
//                MusicManager.class
//                        .getResource("/audio/bgm.mp3")
//                        .toExternalForm()
//        );

        MediaPlayer player = new MediaPlayer(media);

        player.setCycleCount(MediaPlayer.INDEFINITE);

        player.play();
    }

}
