package com.github.naxos84.spaceshooter.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.github.naxos84.spaceshooter.SpaceShooter;
import com.github.naxos84.spaceshooter.model.Asteroid;

import java.util.Iterator;

public class GameScreen implements Screen {

    private final SpaceShooter game;

    private Texture shipImage;
    private Rectangle ship;
    private Texture asteroidTex;
    private Texture laserImage;

    private Sound shootSound;
    private Music backgroundMusic;

    private OrthographicCamera camera;

    private Array<Asteroid> asteroids;
    private long lastAsteroidSpawn;
    private Array<Rectangle> lasers;
    private long lastLaserSpawn;

    private ShapeRenderer sRenderer;

    private final boolean debugMode;

    public GameScreen(final SpaceShooter game, final boolean debugMode) {
        this.game = game;
        this.debugMode = debugMode;
        if (debugMode) {
            sRenderer = new ShapeRenderer();
        }
        Gdx.app.log("SpaceShooter", "creating things.");
        shipImage = new Texture("images/player/playerShip1_blue.png");
        asteroidTex = new Texture("images/meteors/meteorBrown_big1.png");
        laserImage = new Texture("images/lasers/laserBlue01.png");
        shootSound = Gdx.audio.newSound(Gdx.files.internal("audio/laser5.ogg"));
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/DST-DustLoop.mp3"));

        backgroundMusic.setLooping(true);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);

        ship = new Rectangle();
        ship.width = 64;
        ship.height = 64;
        ship.x = 800 / 2 - ship.width / 2;
        ship.y = 600 / 2 - ship.height / 2;

        asteroids = new Array<>();

        lasers = new Array<>();
    }

    private void spawnAsteroid() {
        Asteroid asteroid = new Asteroid(asteroidTex, MathUtils.random(360));
        asteroid.x = 810;
        asteroid.y = MathUtils.random(10, 600 - 64);
        asteroid.width = 64;
        asteroid.height = 64;
        asteroids.add(asteroid);
        lastAsteroidSpawn = TimeUtils.nanoTime();
    }

    private void spawnLaser() {
        Rectangle laser = new Rectangle();
        laser.x = ship.x + ship.width;
        laser.y = ship.y + ship.height / 2;
        laser.width = 27;
        laser.height = 9;
        lasers.add(laser);
        lastLaserSpawn = TimeUtils.nanoTime();
        if (game.getGamePreferences().isSoundEnabled()) {
            shootSound.play(game.getGamePreferences().getSoundVolume());
        }
    }

    @Override
    public void show() {
        if (game.getGamePreferences().isMusicEnabled()) {
            backgroundMusic.setVolume(game.getGamePreferences().getMusicVolume());
            backgroundMusic.play();
        }
    }

    @Override
    public void render(final float delta) {
        Gdx.gl.glClearColor(0, 0, .2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        if (debugMode) {
            sRenderer.begin(ShapeRenderer.ShapeType.Line);
            sRenderer.setColor(Color.RED);
            sRenderer.rect(ship.x, ship.y, ship.width, ship.height);
            for (Asteroid asteroid : asteroids) {
                sRenderer.rect(asteroid.x, asteroid.y, asteroid.width, asteroid.height);
            }
            for (Rectangle laser : lasers) {
                sRenderer.rect(laser.x, laser.y, laser.width, laser.height);
            }
            sRenderer.end();
        }

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(shipImage, ship.x, ship.y, ship.width, ship.height);
        for (Asteroid asteroid : asteroids) {
            game.batch.draw(asteroid.getTextureRegion(), asteroid.x, asteroid.y, asteroid.width / 2, asteroid.height / 2, asteroid.width, asteroid.height, 1, 1, asteroid.getRotation());
        }
        for (Rectangle laser : lasers) {
            game.batch.draw(laserImage, laser.x, laser.y, laser.width, laser.height);
        }
        game.batch.end();

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            ship.x -= debugMode ? 1 : 100 * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            ship.x += debugMode ? 1 : 100 * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            ship.y += debugMode ? 1 : 100 * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            ship.y -= debugMode ? 1 : 100 * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            if (TimeUtils.nanoTime() - lastLaserSpawn > 100000000) {
                spawnLaser();
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            quitGame();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_0)) {
            spawnAsteroid();
        }


        ship.x = MathUtils.clamp(ship.x, 0, 800 - ship.width);
        ship.y = MathUtils.clamp(ship.y, ship.height, 600);

        if (TimeUtils.nanoTime() - lastAsteroidSpawn > 1000000000) {
            spawnAsteroid();
        }

        for (Iterator<Asteroid> asteroidsIterator = asteroids.iterator(); asteroidsIterator.hasNext(); ) {
            Asteroid asteroid = asteroidsIterator.next();
            asteroid.x -= debugMode ? 1 : 200 * delta;
            if (asteroid.x < 0) {
                asteroidsIterator.remove();
            } else if (asteroid.overlaps(ship)) {
                quitGame();
            }

        }

        for (Iterator<Rectangle> lasersIterator = lasers.iterator(); lasersIterator.hasNext(); ) {
            Rectangle laser = lasersIterator.next();
            laser.x += debugMode ? 1 : 600 * delta;
            boolean laserRemoved = false;
            if (laser.x > 800) {
                lasersIterator.remove();
                laserRemoved = true;
            }
            for (Iterator<Asteroid> asteroidsIterator = asteroids.iterator(); asteroidsIterator.hasNext(); ) {
                Asteroid asteroid = asteroidsIterator.next();
                if (laser.overlaps(asteroid)) {
                    asteroidsIterator.remove();
                    if (!laserRemoved) {
                        lasersIterator.remove();
                    }
                }
            }
        }
    }

    private void quitGame() {
        game.setScreen(new MainMenuScreen(game, debugMode));
        dispose();
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
        backgroundMusic.stop();
    }

    @Override
    public void dispose() {
        shipImage.dispose();
        asteroidTex.dispose();
        laserImage.dispose();
        shootSound.stop();
        shootSound.dispose();
        backgroundMusic.stop();
        backgroundMusic.dispose();
    }
}
