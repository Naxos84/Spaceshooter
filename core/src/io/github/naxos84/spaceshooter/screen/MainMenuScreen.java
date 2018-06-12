package io.github.naxos84.spaceshooter.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.naxos84.spaceshooter.SpaceShooter;

import java.util.Locale;

public class MainMenuScreen implements Screen {

    private final SpaceShooter game;
    private final boolean debugMode;

    private final Stage stage;

    private TextButton startGame;
    private TextButton preferences;
    private TextButton exitGame;

    public MainMenuScreen(final SpaceShooter game, final boolean debugMode) {
        this.game = game;
        this.debugMode = debugMode;

        stage = new Stage(new ScreenViewport());
    }

    @Override
    public void show() {
        //Gdx.app.getPreferences("MY_PREFS").putString("LAST_START", "now").flush();


        Gdx.input.setInputProcessor(stage);
        Table table = new Table();
        table.setFillParent(true);
        table.setDebug(debugMode);

        stage.addActor(table);

        final Skin skin = new Skin(Gdx.files.internal("skin/kenney/kenney-test2.json")); //star-soldier/star-soldier-ui.json"));

        startGame = new TextButton(game.bundle.get("KEY_START_GAME"), skin);
        startGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new GameScreen(game, debugMode));
                dispose();
            }
        });

        preferences = new TextButton(game.bundle.get("KEY_PREFERENCES"), skin);
        preferences.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new PreferencesScreen(game, debugMode));
                dispose();
            }
        });

        exitGame = new TextButton(game.bundle.get("KEY_EXIT_GAME"), skin);
        exitGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        table.add(startGame).fillX().uniformX();
        table.row().pad(10, 0, 10, 0);
        table.add(preferences).fill().uniformX();
        table.row();
        table.add(exitGame).fillX().uniformX();

        game.playMenuMusic();

    }

    @Override
    public void render(final float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isKeyJustPressed(Input.Keys.G)) {
            changeLocale(Locale.GERMANY);

        } else if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            changeLocale(Locale.getDefault());
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            changeLocale(Locale.UK);
        }

        stage.act(Math.min(delta, 1 / 30f));
        stage.draw();
    }

    private void changeLocale(final Locale locale) {
        game.setLocale(locale);
        startGame.setText(game.bundle.get("KEY_START_GAME"));
        exitGame.setText(game.bundle.get("KEY_EXIT_GAME"));
    }

    @Override
    public void resize(final int width, final int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
