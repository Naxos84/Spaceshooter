package io.github.naxos84.spaceshooter.manager;

import com.badlogic.gdx.audio.Music;
import io.github.naxos84.spaceshooter.SpaceShooter;

public class AudioManager {

    private final SpaceShooter game;
    private final SpaceshooterAssetManager assetManager;

    private Music gameMusic;
    private Music menuMusic;

    private float musicVolume;
    private float soundVolume;

    public AudioManager(final SpaceShooter game, final SpaceshooterAssetManager assetManager) {
        this.game = game;
        this.assetManager = assetManager;
    }

    public void load() {
        gameMusic = assetManager.getGameMusic();
        gameMusic.setLooping(true);
        menuMusic = assetManager.getMenuMusic();
        menuMusic.setLooping(true);
    }

    public void playMenuMusic() {
        if (game.getGamePreferences().isMusicEnabled()) {
            if (!menuMusic.isPlaying()) {
                gameMusic.stop();
                menuMusic.setVolume(game.getGamePreferences().getMusicVolume());
                menuMusic.play();
            }
        } else {
            gameMusic.stop();
            menuMusic.stop();
        }
    }

    public void playGameMusic() {
        if (game.getGamePreferences().isMusicEnabled()) {
            if (!gameMusic.isPlaying()) {
                menuMusic.stop();
                gameMusic.setVolume(game.getGamePreferences().getMusicVolume());
                gameMusic.play();
            }
        } else {
            gameMusic.stop();
            menuMusic.stop();
        }
    }

    public void setMusicVolume(final float volume) {
        if (menuMusic != null) {
            menuMusic.setVolume(volume);
        }
        if (gameMusic != null) {
            gameMusic.setVolume(volume);
        }
    }

    public void setSoundVolume(final float volume) {

    }

    public void enableMusic(final boolean enabled) {

    }

    public void enableSound(final boolean enabled) {

    }

    public void dispose() {
        gameMusic.dispose();
        menuMusic.dispose();
    }
}
