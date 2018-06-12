package io.github.naxos84.spaceshooter.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.naxos84.spaceshooter.SpaceShooter;

public class PreferencesScreen implements Screen {

    private final SpaceShooter game;
    private final boolean debugMode;
    private final Stage stage;

    public PreferencesScreen(final SpaceShooter game, final boolean debugMode) {
        this.game = game;
        this.debugMode = debugMode;

        stage = new Stage(new ScreenViewport());
    }
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        Table table = new Table();
        table.setFillParent(true);
        table.setDebug(debugMode);

        stage.addActor(table);

        final Skin skin = new Skin(Gdx.files.internal("skin/kenney/kenney-test2.json"));

        final Slider musicVolume = new Slider(0, 1, .01f, false, skin);
        musicVolume.setValue(game.getGamePreferences().getMusicVolume());
        musicVolume.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                game.getGamePreferences().setMusicVolume(musicVolume.getValue());
                return false;
            }
        });
        final CheckBox musicEnabled = new CheckBox(game.bundle.get("KEY_PREF_MUSIC_ENABLED"), skin);
        musicEnabled.setChecked(game.getGamePreferences().isMusicEnabled());
        musicEnabled.addListener(new EventListener() {

            @Override
            public boolean handle(Event event) {
                game.getGamePreferences().setMusicEnabled(musicEnabled.isChecked());
                return false;
            }
        });

        final Slider soundVolume = new Slider(0, 1, .01f, false, skin);
        soundVolume.setValue(game.getGamePreferences().getSoundVolume());
        soundVolume.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                game.getGamePreferences().setSoundVolume(soundVolume.getValue());
                return false;
            }
        });
        final CheckBox soundEnabled = new CheckBox(game.bundle.get("KEY_PREF_SOUND_ENABLED"), skin);
        soundEnabled.setChecked(game.getGamePreferences().isSoundEnabled());
        soundEnabled.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                game.getGamePreferences().setSoundEnabled(soundEnabled.isChecked());
                return false;
            }
        });

        final TextButton back = new TextButton(game.bundle.get("KEY_UI_BACK"), skin);
        back.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenuScreen(game, debugMode));
                dispose();
            }
        });

        table.add(musicVolume).fillX().uniformX();
        table.row().pad(10, 0, 10, 0);
        table.add(musicEnabled).fill().uniformX();
        table.row();
        table.add(soundVolume).fillX().uniformX();
        table.row();
        table.add(soundEnabled).fill().uniformX();
        table.row();
        table.add(back).fill().uniformX();

        game.playMenuMusic();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Math.min(delta, 1 / 30f));
        stage.draw();
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
