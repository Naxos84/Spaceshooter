package io.github.naxos84.spaceshooter;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.I18NBundle;
import io.github.naxos84.spaceshooter.manager.AudioManager;
import io.github.naxos84.spaceshooter.manager.ScreenManager;
import io.github.naxos84.spaceshooter.manager.SpaceshooterAssetManager;
import io.github.naxos84.spaceshooter.screen.MainMenuScreen;

import java.util.Locale;

public class SpaceShooter extends Game {

    public static final float SCREEN_WIDTH = 800;
    public static final float HEIGHT = 600;

    public SpriteBatch batch;
    public BitmapFont font;
    private final boolean debugMode;
    private Locale locale;
    private FileHandle baseFileHandle;
    public I18NBundle bundle;
    private GamePreferences gamePreferences;

    private SpaceshooterAssetManager assetManager = new SpaceshooterAssetManager();
    private ScreenManager screenManager = new ScreenManager();
    private AudioManager audioManager;

    public SpaceShooter(final boolean debugMode) {
        this.debugMode = debugMode;
        locale = Locale.getDefault();
        gamePreferences = new GamePreferences();
        audioManager = new AudioManager(this, assetManager);
    }

    public void setLocale(final Locale locale) {
        this.locale = locale;
        bundle = I18NBundle.createBundle(baseFileHandle, locale);
    }

    public GamePreferences getGamePreferences() {
        return gamePreferences;
    }

    @Override
    public void create() {
        screenManager.loadScreens(this, debugMode);


        batch = new SpriteBatch();
        font = new BitmapFont();
        baseFileHandle = Gdx.files.internal("i18n/lang");
        bundle = I18NBundle.createBundle(baseFileHandle, locale);
        this.setScreen(screenManager.getScreen(ScreenManager.LOADING));

    }



    @Override
    public void render() {
        super.render();
    }


    @Override
    public void dispose() {
        this.screen.dispose();
        assetManager.dispose();
        audioManager.dispose();
    }

    public void changeScreen(int index) {
        setScreen(screenManager.getScreen(index));
    }

    public SpaceshooterAssetManager getAssetManager() {
        return assetManager;
    }

    public AudioManager getAudioManager() {
        return audioManager;
    }
}
