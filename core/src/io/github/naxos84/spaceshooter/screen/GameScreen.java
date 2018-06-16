package io.github.naxos84.spaceshooter.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import io.github.naxos84.spaceshooter.SpaceShooter;
import io.github.naxos84.spaceshooter.controller.KeyboardController;
import io.github.naxos84.spaceshooter.manager.AudioManager;
import io.github.naxos84.spaceshooter.manager.ScreenManager;
import io.github.naxos84.spaceshooter.manager.SpaceshooterAssetManager;
import io.github.naxos84.spaceshooter.model.*;
import io.github.naxos84.spaceshooter.overlay.GameOver;
import io.github.naxos84.spaceshooter.renderer.AsteroidRenderer;
import io.github.naxos84.spaceshooter.renderer.EnemyRenderer;
import io.github.naxos84.spaceshooter.renderer.LaserRenderer;
import io.github.naxos84.spaceshooter.renderer.ShipRenderer;

import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class GameScreen implements Screen {

    private final SpaceShooter game;
    private final boolean debugMode;
    private final SpaceshooterAssetManager assetManager;
    private final Random random = new Random();
    private final KeyboardController keyboardController = new KeyboardController();
    private Ship ship;
    private ShipRenderer shipRenderer;
    private OrthographicCamera camera;
    private Array<Asteroid> asteroids;
    private AsteroidRenderer asteroidsRenderer;
    private long lastAsteroidSpawn;
    private Array<Laser> lasers;
    private Array<Laser> enemyLasers;
    private Array<Enemy> enemies;
    private LaserRenderer laserRenderer;
    private long lastLaserSpawn;
    private ParticleEffect effect;
    private ShapeRenderer sRenderer;
    private Score score;
    private float energyTimer = 0f;
    private GameOver gameOver;
    private TextureRegion healthBarLeft;
    private TextureRegion healthBarMid;
    private TextureRegion healthBarRight;
    private TextureRegion energyBarLeft;
    private TextureRegion energyBarMid;
    private TextureRegion energyBarRight;
    private final AudioManager audioManager;
    private EnemyRenderer enemyRenderer;
    private int spawnCount = 0;


    public GameScreen(final SpaceShooter game, AudioManager audioManager,  final boolean debugMode) {
        this.game = game;
        this.debugMode = debugMode;
        this.assetManager = game.getAssetManager();

        sRenderer = new ShapeRenderer();

        Gdx.app.log("SpaceShooter", "creating things.");

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);

        ship = new Ship(800 / 2 - 64 / 2, 600 / 2 - 64 / 2, 64, 64);

        asteroids = new Array<>();
        lasers = new Array<>();
        enemies = new Array<>();

        gameOver = new GameOver();
        this.audioManager = audioManager;

    }

    private void spawnHazard() {
        if (spawnCount > 10) {
            spawnEnemy();
            spawnCount = 0;
        } else {
            spawnAsteroid();
        }
        lastAsteroidSpawn = TimeUtils.millis();
        spawnCount++;
    }

    private void spawnEnemy() {
        int id = random.nextInt(assetManager.getNumberOfEnemies());
        final String regionName = assetManager.getEnemy(id).name;
        float width = assetManager.getEnemy(id).getRegionWidth();
        float height = assetManager.getEnemy(id).getRegionHeight();
        Enemy enemy = new Enemy(id, 810, MathUtils.random(10, 600 - height), width - 10, height - 10, MathUtils.random(360));
        enemy.setHealth(10);
        enemies.add(enemy);
    }

    private void spawnAsteroid() {
        int id = random.nextInt(assetManager.getNumberOfAsteroids());
        final String regionName = assetManager.getAsteroid(id).name;
        float asteroidHealth = 100f;
        if (regionName.contains("big")) {
            asteroidHealth = 7f;
        } else if (regionName.contains("med")) {
            asteroidHealth = 5f;
        } else if (regionName.contains("small")) {
            asteroidHealth = 3f;
        } else if (regionName.contains("tiny")) {
            asteroidHealth = 1f;
        }
        float width = assetManager.getAsteroid(id).getRegionWidth();
        float height = assetManager.getAsteroid(id).getRegionHeight();
        Asteroid asteroid = new Asteroid(id, 810, MathUtils.random(10, 600 - height), width, height, MathUtils.random(360));
        asteroid.setHealth(MathUtils.roundPositive(asteroidHealth));
        asteroids.add(asteroid);
    }

    private void spawnLaser() {
        if (ship.getCurrentEnergy() > 10) {
            Laser laser = new Laser(ship.getX() + ship.getWidth(), ship.getY() + ship.getHeight() / 2, 27, 9);
            lasers.add(laser);
            lastLaserSpawn = TimeUtils.nanoTime();
            ship.reduceEnergy(10);
            if (game.getGamePreferences().isSoundEnabled()) {
                assetManager.getLaserSound().play(game.getGamePreferences().getSoundVolume());
            }
        }
    }

    @Override
    public void show() {
        shipRenderer = new ShipRenderer(assetManager);
        laserRenderer = new LaserRenderer(assetManager);
        asteroidsRenderer = new AsteroidRenderer(assetManager);
        enemyRenderer = new EnemyRenderer(assetManager);

        Gdx.input.setInputProcessor(keyboardController);
        effect = new ParticleEffect();
        effect.load(Gdx.files.internal("images/particles/meteor_explosion.p"), Gdx.files.internal("images/meteors"));
        audioManager.playGameMusic();
        score = new Score();


        healthBarLeft = assetManager.getHealthBarLeft();
        healthBarMid = assetManager.getHealthBarMid();
        healthBarRight = assetManager.getHealthBarRight();
        energyBarLeft = assetManager.getEnergyBarLeft();
        energyBarMid = assetManager.getEnergyBarMid();
        energyBarRight = assetManager.getEnergyBarRight();
        resetGame();
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
            for (Enemy enemy : enemies) {
                enemyRenderer.renderDebug(sRenderer, enemy);
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
        for (final Enemy enemy : enemies) {
            enemyRenderer.render(game.batch, enemy);
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
        if (keyboardController.isLeftPressed()) {
            ship.moveLeft(delta);
        }
        if (keyboardController.isRightPressed()) {
            ship.moveRight(delta);
        }
        if (keyboardController.isUpPressed()) {
            ship.moveUp(delta);
        }
        if (keyboardController.isDownPressed()) {
            ship.moveDown(delta);
        }
        if (keyboardController.isSpacePressed()) {
            if (TimeUtils.nanoTime() - lastLaserSpawn > 100000000) {
                spawnLaser();
            }
        }
        if (keyboardController.isEscapePressed()) {
            quitGame();
        }

        if (keyboardController.isNumpad0Pressed()) {
            spawnAsteroid();
        }
    }

    private void updateObjects(final float delta) {
        energyTimer += delta;
        if (energyTimer >= .1f) {
            ship.addEnergy(5);
            energyTimer -= .1f;
        }

        ship.updatePosition();

        if (TimeUtils.millis() - lastAsteroidSpawn > 500) {
            spawnHazard();
        }
    }

    private void checkCollisions(final float delta) {
        for (Iterator<Asteroid> asteroidsIterator = new Array.ArrayIterator<>(asteroids); asteroidsIterator.hasNext(); ) {
            Asteroid asteroid = asteroidsIterator.next();
            asteroid.updatePosition(delta);
            if (asteroid.isDead()) {
                asteroidsIterator.remove();
            } else if (ship.isAlive() && asteroid.overlaps(ship.getCollisionBox())) {
                ship.reduceHealth(asteroid.getCurrentHealth());
                asteroidsIterator.remove();
                effect.setPosition(asteroid.getX(), asteroid.getY());
                effect.start();
                if (game.getGamePreferences().isSoundEnabled()) {
                    assetManager.getAsteroidExplosion().play(game.getGamePreferences().getSoundVolume());
                }
            }

        }

        for (Iterator<Enemy> enemiesIterator = new Array.ArrayIterator<>(enemies); enemiesIterator.hasNext(); ) {
            Enemy enemy = enemiesIterator.next();
            enemy.updatePosition(delta);
            if (enemy.isDead()) {
                enemiesIterator.remove();
            } else if (ship.isAlive() && enemy.overlaps(ship.getCollisionBox())) {
                ship.reduceHealth(enemy.getCurrentHealth());
                enemiesIterator.remove();
                effect.setPosition(enemy.getX(), enemy.getY());
                effect.start();
                if (game.getGamePreferences().isSoundEnabled()) {
                    assetManager.getAsteroidExplosion().play(game.getGamePreferences().getSoundVolume());
                }
            }
        }
        for (Iterator<Laser> lasersIterator = new Array.ArrayIterator<>(lasers); lasersIterator.hasNext(); ) {
            Laser laser = lasersIterator.next();
            laser.updatePosition(delta);
            boolean laserRemoved = false;
            if (laser.isDead()) {
                lasersIterator.remove();
                laserRemoved = true;
            }
            for (Iterator<Asteroid> asteroidsIterator = new Array.ArrayIterator<>(asteroids); asteroidsIterator.hasNext(); ) {
                Asteroid asteroid = asteroidsIterator.next();
                if (laser.overlaps(asteroid.getCollisionBox())) {
                    asteroid.reduceHealth(2);
                    if (asteroid.isDead()) {
                        effect.setPosition(asteroid.getX(), asteroid.getY());
                        effect.start();
                        score.add(1);
                        if (game.getGamePreferences().isSoundEnabled()) {
                            assetManager.getAsteroidExplosion().play(game.getGamePreferences().getSoundVolume());
                        }
                        asteroidsIterator.remove();
                    }
                    if (!laserRemoved) {
                        lasersIterator.remove();
                        laserRemoved = true;
                    }
                }
            }
            for (Iterator<Enemy> enemyIterator = new Array.ArrayIterator<>(enemies); enemyIterator.hasNext(); ) {
                Enemy enemy = enemyIterator.next();
                if (laser.overlaps(enemy.getCollisionBox())) {
                    enemy.reduceHealth(2);
                    if (enemy.isDead()) {
                        effect.setPosition(enemy.getX(), enemy.getY());
                        effect.start();
                        score.add(1);
                        if (game.getGamePreferences().isSoundEnabled()) {
                            assetManager.getAsteroidExplosion().play(game.getGamePreferences().getSoundVolume());
                        }
                        enemyIterator.remove();
                    }
                    if (!laserRemoved) {
                        lasersIterator.remove();
                        laserRemoved = true;
                    }
                }
            }
        }
    }

    private void renderGameOver(final float delta) {
        gameOver.render(game.batch, game.bundle, delta);
    }

    private void quitGame() {
        game.changeScreen(ScreenManager.MENU);
    }

    private void resetGame() {
        ship.addHealth(Ship.MAX_HEALTH);
        ship.addEnergy(Ship.MAX_ENERGY);
        ship.setPosition(800 / 2 - 64 / 2, 600 / 2 - 64 / 2);
        asteroids.clear();
        lasers.clear();
        score.reset();
        enemies.clear();
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
        assetManager.getAsteroidExplosion().stop();
        assetManager.getLaserSound().stop();
    }

    @Override
    public void dispose() {
        assetManager.dispose();
        effect.dispose();
    }
}
