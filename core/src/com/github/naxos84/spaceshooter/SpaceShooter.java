package com.github.naxos84.spaceshooter;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class SpaceShooter extends ApplicationAdapter {

    private Texture shipImage;
    private TextureRegion shipTexReg;
    private Rectangle ship;
    private Texture asteroidTex;
    private Texture laserImage;
    private TextureRegion laserTexReg;

    private Sound shootSound;
    private Music backgroundMusic;

    private OrthographicCamera camera;
    private SpriteBatch batch;

    private Array<Asteroid> asteroids;
    private long lastAsteroidSpawn;
    private Array<Rectangle> lasers;
    private long lastLaserSpawn;

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
        laser.x = ship.x + 64;
        laser.y = ship.y - 64 / 2 + 5;
        laser.width = 9;
        laser.height = 27;
        lasers.add(laser);
        lastLaserSpawn = TimeUtils.nanoTime();
        shootSound.play();
    }


    @Override
    public void create() {
        Gdx.app.log("SpaceShooter", "creating things.");
        shipImage = new Texture("images/player/playerShip1_blue.png");
        shipTexReg = new TextureRegion(shipImage);
        asteroidTex = new Texture("images/meteors/meteorBrown_big1.png");
        laserImage = new Texture("images/lasers/laserBlue01.png");
        laserTexReg = new TextureRegion(laserImage);
        shootSound = Gdx.audio.newSound(Gdx.files.internal("audio/laser5.ogg"));
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/DST-DustLoop.mp3"));

        backgroundMusic.setLooping(true);
        backgroundMusic.play();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);

        batch = new SpriteBatch();

        ship = new Rectangle();
        ship.x = 800 / 2 - 64 / 2;
        ship.y = 600 / 2 - 64 / 2;
        ship.width = 64;
        ship.height = 64;

        asteroids = new Array<Asteroid>();
        spawnAsteroid();

        lasers = new Array<Rectangle>();
        spawnLaser();

    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, .2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(shipTexReg, ship.x, ship.y, 0, 0, ship.width, ship.height, 1, 1, 270);
        for (Asteroid asteroid : asteroids) {
            batch.draw(asteroid.getTextureRegion(), asteroid.x, asteroid.y, 0, 0, asteroid.width, asteroid.height, 1, 1, asteroid.getRotation());
        }
        for (Rectangle laser : lasers) {
            batch.draw(laserTexReg, laser.x, laser.y, 0, 0, laser.width, laser.height, 1, 1, 270);
        }
        batch.end();

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            ship.x -= 100 * Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            ship.x += 100 * Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            ship.y += 100 * Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            ship.y -= 100 * Gdx.graphics.getDeltaTime();
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

        Iterator<Asteroid> asteroidsIterator = asteroids.iterator();
        while (asteroidsIterator.hasNext()) {
            Asteroid asteroid = asteroidsIterator.next();
            asteroid.x -= 200 * Gdx.graphics.getDeltaTime();
            if (asteroid.x < 0) {
                asteroidsIterator.remove();
            }
        }

        Iterator<Rectangle> lasersIterator = lasers.iterator();
        asteroidsIterator = asteroids.iterator();
        while (lasersIterator.hasNext()) {
            Rectangle laser = lasersIterator.next();
            laser.x += 600 * Gdx.graphics.getDeltaTime();
            if (laser.x > 800) {
                lasersIterator.remove();
            }
            while (asteroidsIterator.hasNext()) {
                Asteroid asteroid = asteroidsIterator.next();
                if (laser.overlaps(asteroid)) {
                    asteroidsIterator.remove();
                    lasersIterator.remove();
                }
            }
        }

        asteroidsIterator = asteroids.iterator();
        while (asteroidsIterator.hasNext()) {
            Rectangle asteroid = asteroidsIterator.next();
            if (asteroid.overlaps(ship)) {
                asteroidsIterator.remove();
                quitGame();
            }
        }


    }

    private void quitGame() {
        Gdx.app.exit();
    }

    @Override
    public void dispose() {
        batch.dispose();
        shipImage.dispose();
        asteroidTex.dispose();
        laserImage.dispose();
        shootSound.stop();
        shootSound.dispose();
        backgroundMusic.stop();
        backgroundMusic.dispose();


    }

}