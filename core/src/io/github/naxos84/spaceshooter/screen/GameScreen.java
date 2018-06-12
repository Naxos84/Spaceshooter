package io.github.naxos84.spaceshooter.screen;

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
import io.github.naxos84.spaceshooter.SpaceShooter;
import io.github.naxos84.spaceshooter.model.Asteroid;
import io.github.naxos84.spaceshooter.model.Laser;
import io.github.naxos84.spaceshooter.model.Score;
import io.github.naxos84.spaceshooter.model.Ship;
import io.github.naxos84.spaceshooter.overlay.GameOver;
import io.github.naxos84.spaceshooter.renderer.AsteroidRenderer;
import io.github.naxos84.spaceshooter.renderer.LaserRenderer;
import io.github.naxos84.spaceshooter.renderer.ShipRenderer;

import java.util.Iterator;
import java.util.Random;

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
    private GameOver gameOver;

    private TextureAtlas atlas;
    private TextureRegion healthBarLeft;
    private TextureRegion healthBarMid;
    private TextureRegion healthBarRight;
    private TextureRegion energyBarLeft;
    private TextureRegion energyBarMid;
    private TextureRegion energyBarRight;

    private TextureAtlas asteroidsAtlas;
    private int asteroidsAtlasSize;

    private final Random random = new Random();


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

        asteroids = new Array<>();

        lasers = new Array<>();


        asteroidsAtlas = new TextureAtlas(Gdx.files.internal("textures/asteroids.atlas"));
        asteroidsAtlasSize = asteroidsAtlas.getRegions().size;

        shipRenderer = new ShipRenderer(shipImage);
        asteroidsRenderer = new AsteroidRenderer(asteroidsAtlas.getRegions());
        laserRenderer = new LaserRenderer(laserImage);

        atlas = new TextureAtlas(Gdx.files.internal("textures/bars.atlas"));
        gameOver = new GameOver();

    }

    private void spawnAsteroid() {
        int id = random.nextInt(asteroidsAtlasSize);
        float width = asteroidsAtlas.getRegions().get(id).getRegionWidth();
        float height = asteroidsAtlas.getRegions().get(id).getRegionHeight();
        Asteroid asteroid = new Asteroid(id, 810, MathUtils.random(10, 600 - height), width, height, MathUtils.random(360));
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
        game.batch.setProjectionMatrix(camera.combined);
        if (ship.isDead()) {
            gameOver.show();
            renderDebug();
            game.batch.begin();
            renderObjects(delta);
            renderGameOver(delta);
            game.batch.end();
            updateObjects(delta);
            checkCollisions(delta);
            if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
                resetGame();
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                quitGame();
            }

        } else {
            gameOver.hide();
            renderDebug();
            game.batch.begin();
            renderObjects(delta);
            game.batch.end();
            handleInput(delta);
            updateObjects(delta);
            checkCollisions(delta);
        }

    }

    private void renderDebug() {
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
    }

    private void renderObjects(final float delta) {
        score.render(game.batch, game.bundle);
        effect.draw(game.batch, delta);
        shipRenderer.render(game.batch, ship);
        for (Asteroid asteroid : asteroids) {
            asteroidsRenderer.render(game.batch, asteroid); //TODO add rotation back in
        }
        for (Laser laser : lasers) {
            laserRenderer.render(game.batch, laser);
        }
        float healthBarWidth = (float) ship.getCurrentHealth() / Ship.MAX_HEALTH * 200f;
        game.batch.draw(healthBarLeft, SpaceShooter.SCREEN_WIDTH - 220, SpaceShooter.HEIGHT - 40, 6, 15);
        game.batch.draw(healthBarMid, SpaceShooter.SCREEN_WIDTH - 214, SpaceShooter.HEIGHT - 40, healthBarWidth, 15);
        game.batch.draw(healthBarRight, SpaceShooter.SCREEN_WIDTH - 214 + healthBarWidth, SpaceShooter.HEIGHT - 40, 6, 15);

        float energyBarWidth = (float) ship.getCurrentEnergy() / Ship.MAX_ENERGY * 200f;
        game.batch.draw(energyBarLeft, SpaceShooter.SCREEN_WIDTH - 220, SpaceShooter.HEIGHT - 24, 6, 15);
        game.batch.draw(energyBarMid, SpaceShooter.SCREEN_WIDTH - 214, SpaceShooter.HEIGHT - 24, energyBarWidth, 15);
        game.batch.draw(energyBarRight, SpaceShooter.SCREEN_WIDTH - 214 + energyBarWidth, SpaceShooter.HEIGHT - 24, 6, 15);
    }

    private void handleInput(final float delta) {
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
    }

    private void updateObjects(final float delta) {
        energyTimer += delta;
        if (energyTimer >= .1f) {
            ship.addEnergy(3);
            energyTimer -= .1f;
        }

        ship.updatePosition();

        if (TimeUtils.nanoTime() - lastAsteroidSpawn > 500000000) {
            spawnAsteroid();
        }
    }

    private void checkCollisions(final float delta) {
        for (Iterator<Asteroid> asteroidsIterator = asteroids.iterator(); asteroidsIterator.hasNext(); ) {
            Asteroid asteroid = asteroidsIterator.next();
            asteroid.updatePosition(delta);
            if (asteroid.isDead()) {
                asteroidsIterator.remove();
            } else if (ship.isAlive() && asteroid.overlaps(ship.getCollisionBox())) {
                ship.reduceHealth(10);
                asteroidsIterator.remove();
                effect.setPosition(asteroid.getX(), asteroid.getY());
                effect.start();
                if (game.getGamePreferences().isSoundEnabled()) {
                    explosionSound.play(game.getGamePreferences().getSoundVolume());
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

    private void renderGameOver(final float delta) {
        gameOver.render(game.batch, game.bundle, delta);
    }

    private void quitGame() {
        game.setScreen(new MainMenuScreen(game, debugMode));
        dispose();
    }

    private void resetGame() {
        ship.addHealth(Ship.MAX_HEALTH);
        ship.addEnergy(Ship.MAX_ENERGY);
        ship.setPosition(800 / 2 - 64 / 2, 600 / 2 - 64 / 2);
        asteroids.clear();
        lasers.clear();
        score.reset();
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
