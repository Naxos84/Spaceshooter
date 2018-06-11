package com.github.naxos84.spaceshooter.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.github.naxos84.spaceshooter.SpaceShooter;
import com.github.naxos84.spaceshooter.model.Asteroid;
import com.github.naxos84.spaceshooter.model.Laser;
import com.github.naxos84.spaceshooter.model.Score;
import com.github.naxos84.spaceshooter.model.Ship;
import com.github.naxos84.spaceshooter.renderer.AsteroidRenderer;
import com.github.naxos84.spaceshooter.renderer.LaserRenderer;
import com.github.naxos84.spaceshooter.renderer.ShipRenderer;

import java.util.Iterator;

public class GameScreen implements Screen {

    private final SpaceShooter game;
    private final boolean debugMode;
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
    private Score score;
    private float energyTimer = 0f;

    private TextureAtlas atlas;
    private TextureRegion healthBarLeft;
    private TextureRegion healthBarMid;
    private TextureRegion healthBarRight;
    private TextureRegion energyBarLeft;
    private TextureRegion energyBarMid;
    private TextureRegion energyBarRight;


    public GameScreen(final SpaceShooter game, final boolean debugMode) {
        this.game = game;
        this.debugMode = debugMode;

        sRenderer = new ShapeRenderer();

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

        atlas = new TextureAtlas(Gdx.files.internal("textures/bars.atlas"));

    }

    private void spawnAsteroid() {
        Asteroid asteroid = new Asteroid(810, MathUtils.random(10, 600 - 64), 64, 64, MathUtils.random(360));
        asteroids.add(asteroid);
        lastAsteroidSpawn = TimeUtils.nanoTime();
    }

    private void spawnLaser() {
        if (ship.getCurrentEnergy() > 10) {
            Laser laser = new Laser(ship.getX() + ship.getWidth(), ship.getY() + ship.getHeight() / 2, 27, 9);
            lasers.add(laser);
            lastLaserSpawn = TimeUtils.nanoTime();
            ship.reduceEnergy(10);
            if (game.getGamePreferences().isSoundEnabled()) {
                shootSound.play(game.getGamePreferences().getSoundVolume());
            }
        }
    }

    @Override
    public void show() {
        effect = new ParticleEffect();
        effect.load(Gdx.files.internal("images/particles/meteor_explosion.p"), Gdx.files.internal("images/meteors"));
        game.playGameMusic();
        score = new Score();


        healthBarLeft = atlas.findRegion("barHorizontal_red_left");
        healthBarMid = atlas.findRegion("barHorizontal_red_mid");
        healthBarRight = atlas.findRegion("barHorizontal_red_right");
        energyBarLeft = atlas.findRegion("barHorizontal_blue_left");
        energyBarMid = atlas.findRegion("barHorizontal_blue_mid");
        energyBarRight = atlas.findRegion("barHorizontal_blue_right");
    }

    @Override
    public void render(final float delta) {
        Gdx.gl.glClearColor(0, 0, .2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        sRenderer.begin(ShapeRenderer.ShapeType.Line);
        if (debugMode) {
            sRenderer.setColor(Color.RED);
            shipRenderer.renderDebug(sRenderer, ship);
            for (Asteroid asteroid : asteroids) {
                asteroidsRenderer.renderDebug(sRenderer, asteroid);
            }
            for (Laser laser : lasers) {
                laserRenderer.renderDebug(sRenderer, laser);
            }
        }
        sRenderer.end();

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        score.render(game.batch);
        effect.draw(game.batch, delta);
        shipRenderer.render(game.batch, ship);
        for (Asteroid asteroid : asteroids) {
            asteroidsRenderer.render(game.batch, asteroid); //TODO add rotation back in
        }
        for (Laser laser : lasers) {
            laserRenderer.render(game.batch, laser);
        }
        float healthBarWidth = ship.getCurrentHealth() * 2f;
        game.batch.draw(healthBarLeft, SpaceShooter.SCREEN_WIDTH - 220, SpaceShooter.HEIGHT - 40, 6, 15);
        game.batch.draw(healthBarMid, SpaceShooter.SCREEN_WIDTH - 214, SpaceShooter.HEIGHT - 40, healthBarWidth, 15);
        game.batch.draw(healthBarRight, SpaceShooter.SCREEN_WIDTH - 214 + healthBarWidth, SpaceShooter.HEIGHT - 40, 6, 15);

        float energyBarWidth = ship.getCurrentEnergy() * 2f;
        game.batch.draw(energyBarLeft, SpaceShooter.SCREEN_WIDTH - 220, SpaceShooter.HEIGHT - 24, 6, 15);
        game.batch.draw(energyBarMid, SpaceShooter.SCREEN_WIDTH - 214, SpaceShooter.HEIGHT - 24, energyBarWidth, 15);
        game.batch.draw(energyBarRight, SpaceShooter.SCREEN_WIDTH - 214 + energyBarWidth, SpaceShooter.HEIGHT - 24, 6, 15);
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

        energyTimer += delta;
        if (energyTimer >= .1f) {
            ship.addEnergy(2);
            energyTimer -= .1f;
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
                ship.reduceHealth(10);
                asteroidsIterator.remove();
                effect.setPosition(asteroid.getX(), asteroid.getY());
                effect.start();
                if (game.getGamePreferences().isSoundEnabled()) {
                    explosionSound.play(game.getGamePreferences().getSoundVolume());
                }
                if (ship.isDead()) {
                    quitGame();
                }
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
                    score.add(1);
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
