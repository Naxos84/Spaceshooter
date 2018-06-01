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
import com.badlogic.gdx.math.Rectangle;

public class SpaceShooter extends ApplicationAdapter {

    private Texture shipImage;
    private TextureRegion shipTexReg;
    private Rectangle ship;
    private Texture asteroidImage;
    private Texture laserImage;

    private Sound shootSound;
    private Music backgroundMusic;

    private OrthographicCamera camera;
    private SpriteBatch batch;


    @Override
    public void create() {
        Gdx.app.log("SpaceShooter", "creating things.");
        shipImage = new Texture("images/player/playerShip1_blue.png");
        shipTexReg = new TextureRegion(shipImage);
        asteroidImage = new Texture("images/meteors/meteorBrown_big1.png");
        laserImage = new Texture("images/lasers/laserBlue01.png");
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



    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, .2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(shipTexReg, ship.x, ship.y, 0, 0, ship.width, ship.height, 1, 1, 270);

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
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }

    }

    @Override
    public void dispose() {
        batch.dispose();
        shipImage.dispose();
        asteroidImage.dispose();
        laserImage.dispose();
        shootSound.stop();
        shootSound.dispose();
        backgroundMusic.stop();
        backgroundMusic.dispose();

    }

}
