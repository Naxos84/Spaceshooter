package com.github.naxos84.spaceshooter.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.collision.CollisionConstants;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.github.naxos84.spaceshooter.SpaceShooter;
import com.github.naxos84.spaceshooter.model.Asteroid;
import com.github.naxos84.spaceshooter.model.Laser;
import com.github.naxos84.spaceshooter.model.Ship;
import com.github.naxos84.spaceshooter.renderer.AsteroidRenderer;
import com.github.naxos84.spaceshooter.renderer.LaserRenderer;
import com.github.naxos84.spaceshooter.renderer.ShipRenderer;

import java.util.Iterator;

public class GameScreen implements Screen {

    private final SpaceShooter game;

    private Texture shipImage;
    private Ship ship;
    private ShipRenderer shipRenderer;
    private Texture asteroidTex;
    private Texture laserImage;

    private Sound shootSound;
    private Sound explosionSound;

    private OrthographicCamera camera;

    private Array<Asteroid> asteroids;
    private AsteroidRenderer asteroidsRenderer;
    private long lastAsteroidSpawn;
    private Array<Laser> lasers;
    private LaserRenderer laserRenderer;
    private long lastLaserSpawn;

    private ParticleEffect effect;

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
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("audio/explosion_asteroid.ogg"));

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);

        ship = new Ship(800 / 2 - 64 / 2, 600 / 2 - 64 / 2, 64, 64);

        asteroids = new Array<Asteroid>();

        lasers = new Array<Laser>();

        shipRenderer = new ShipRenderer(shipImage);
        asteroidsRenderer = new AsteroidRenderer(asteroidTex);
        laserRenderer = new LaserRenderer(laserImage);
    }

    private void spawnAsteroid() {
        Asteroid asteroid = new Asteroid(810,MathUtils.random(10, 600 - 64), 64, 64, MathUtils.random(360));
        asteroids.add(asteroid);
        lastAsteroidSpawn = TimeUtils.nanoTime();
    }

    private void spawnLaser() {
        Laser laser = new Laser(ship.getX() + ship.getWidth(), ship.getY() + ship.getHeight() / 2, 27, 9);
        lasers.add(laser);
        lastLaserSpawn = TimeUtils.nanoTime();
        if (game.getGamePreferences().isSoundEnabled()) {
            shootSound.play(game.getGamePreferences().getSoundVolume());
        }
    }

    @Override
    public void show() {
        effect = new ParticleEffect();
        effect.load(Gdx.files.internal("images/particles/meteor_explosion.p"), Gdx.files.internal("images/meteors"));
        game.playGameMusic();
    }

    @Override
    public void render(final float delta) {
        Gdx.gl.glClearColor(0, 0, .2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        if (debugMode) {
            sRenderer.begin(ShapeRenderer.ShapeType.Line);
            sRenderer.setColor(Color.RED);
            shipRenderer.renderDebug(sRenderer, ship);
            for (Asteroid asteroid : asteroids) {
                asteroidsRenderer.renderDebug(sRenderer, asteroid);
            }
            for (Laser laser : lasers) {
                laserRenderer.renderDebug(sRenderer, laser);
            }
            sRenderer.end();
        }

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        effect.draw(game.batch, delta);
        shipRenderer.render(game.batch, ship);
        for (Asteroid asteroid : asteroids) {
            asteroidsRenderer.render(game.batch, asteroid); //TODO add rotation back in
        }
        for (Laser laser : lasers) {
            laserRenderer.render(game.batch, laser);
        }
        game.batch.end();

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            ship.moveLeft(delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            ship.moveRight(delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            ship.moveUp(delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            ship.moveDown(delta);
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

        ship.updatePosition();

        if (TimeUtils.nanoTime() - lastAsteroidSpawn > 1000000000) {
            spawnAsteroid();
        }

        for (Iterator<Asteroid> asteroidsIterator = asteroids.iterator(); asteroidsIterator.hasNext(); ) {
            Asteroid asteroid = asteroidsIterator.next();
            asteroid.updatePosition(delta);
            if (asteroid.isDead()) {
                asteroidsIterator.remove();
            } else if (asteroid.overlaps(ship.getCollisionBox())) {
                quitGame();
            }

        }

        for (Iterator<Laser> lasersIterator = lasers.iterator(); lasersIterator.hasNext(); ) {
            Laser laser = lasersIterator.next();
            laser.updatePosition(delta);
            boolean laserRemoved = false;
            if (laser.isDead()) {
                lasersIterator.remove();
                laserRemoved = true;
            }
            for (Iterator<Asteroid> asteroidsIterator = asteroids.iterator(); asteroidsIterator.hasNext(); ) {
                Asteroid asteroid = asteroidsIterator.next();
                if (laser.overlaps(asteroid.getCollisionBox())) {
                    effect.setPosition(asteroid.getX(), asteroid.getY());
                    effect.start();
                    if (game.getGamePreferences().isSoundEnabled()) {
                        explosionSound.play(game.getGamePreferences().getSoundVolume());
                    }
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
        explosionSound.stop();
        shootSound.stop();
    }

    @Override
    public void dispose() {
        shipImage.dispose();
        asteroidTex.dispose();
        laserImage.dispose();
        explosionSound.dispose();
        shootSound.dispose();
        effect.dispose();
    }
}
