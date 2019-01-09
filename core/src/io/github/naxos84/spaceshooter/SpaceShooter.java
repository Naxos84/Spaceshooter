package io.github.naxos84.spaceshooter;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.I18NBundle;
import io.github.naxos84.spaceshooter.manager.AudioManager;
import io.github.naxos84.spaceshooter.manager.ScreenManager;
import io.github.naxos84.spaceshooter.manager.SpaceshooterAssetManager;

import java.util.Locale;

public class SpaceShooter extends Game {

    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 600;
    private final boolean debugMode;
    public SpriteBatch batch;
    public BitmapFont font;
    public ShapeRenderer shapeRenderer;
    public I18NBundle bundle;
    private Locale locale;
    private FileHandle baseFileHandle;
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


        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
        font = new BitmapFont(Gdx.files.internal("skin/kenney/kenney-future2.fnt"));
        screenManager.loadScreens(this, debugMode);
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
        batch.dispose();
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
