package com.github.naxos84.spaceshooter.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.github.naxos84.spaceshooter.SpaceShooter;

import java.util.Locale;

public class MainMenuScreen implements Screen {

    private final SpaceShooter game;
    private OrthographicCamera camera;
    private final boolean debugMode;

    public MainMenuScreen(final SpaceShooter game, final boolean debugMode) {
        this.game = game;
        this.debugMode = debugMode;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);

    }

    @Override
    public void show() {

    }

    @Override
    public void render(final float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.font.draw(game.batch, game.bundle.get("KEY_WELCOME"), 100, 150);
        game.font.draw(game.batch, game.bundle.get("KEY_CLICK_TO_BEGIN"), 100, 100);
        game.batch.end();

        if (Gdx.input.isTouched()) {
            game.setScreen(new GameScreen(game, debugMode));
            dispose();
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.G)) {
            game.setLocale(Locale.GERMANY);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            game.setLocale(Locale.getDefault());
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            game.setLocale(Locale.UK);
        }
    }

    @Override
    public void resize(final int width, final int height) {

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

    }
}
