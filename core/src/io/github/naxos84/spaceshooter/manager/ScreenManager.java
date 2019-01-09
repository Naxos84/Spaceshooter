package io.github.naxos84.spaceshooter.manager;

import com.badlogic.gdx.Screen;
import io.github.naxos84.spaceshooter.SpaceShooter;
import io.github.naxos84.spaceshooter.exceptions.UnsupportedScreenException;
import io.github.naxos84.spaceshooter.screen.GameScreen;
import io.github.naxos84.spaceshooter.screen.LoadingScreen;
import io.github.naxos84.spaceshooter.screen.MainMenuScreen;
import io.github.naxos84.spaceshooter.screen.PreferencesScreen;

public class ScreenManager {

    public static final int MENU = 0;
    public static final int PREFERENCES = 1;
    public static final int GAME = 2;
    public static final int LOADING = 3;

    private Screen menuScreen = null;
    private Screen preferencesScreen = null;
    private Screen gameScreen = null;
    private Screen loadingScreen = null;

    public void loadScreens(final SpaceShooter game, final boolean debugMode) {
        if (menuScreen == null) {
            menuScreen = new MainMenuScreen(game, debugMode);
        }
        if (preferencesScreen == null) {
            preferencesScreen = new PreferencesScreen(game, debugMode);
        }
        if (gameScreen == null) {
            gameScreen = new GameScreen(game, game.getAudioManager(), debugMode);
        }
        if (loadingScreen == null) {
            loadingScreen = new LoadingScreen(game, debugMode);
        }
    }

    public Screen getScreen(final int index) {
        switch (index) {
            case MENU:
                return menuScreen;
            case PREFERENCES:
                return preferencesScreen;
            case GAME:
                return gameScreen;
            case LOADING:
                return loadingScreen;
            default:
                throw new UnsupportedScreenException("Screen with index " + index + "does not exist");
        }
    }


}
