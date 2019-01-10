package io.github.naxos84.spaceshooter.screen;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;
import io.github.naxos84.spaceshooter.Families;
import io.github.naxos84.spaceshooter.SpaceShooter;
import io.github.naxos84.spaceshooter.components.*;
import io.github.naxos84.spaceshooter.controller.KeyboardController;
import io.github.naxos84.spaceshooter.listener.PlayerEntityListener;
import io.github.naxos84.spaceshooter.manager.AudioManager;
import io.github.naxos84.spaceshooter.manager.ScreenManager;
import io.github.naxos84.spaceshooter.manager.SpaceshooterAssetManager;
import io.github.naxos84.spaceshooter.model.Score;
import io.github.naxos84.spaceshooter.model.Ship;
import io.github.naxos84.spaceshooter.overlay.GameOver;
import io.github.naxos84.spaceshooter.systems.*;
import io.github.naxos84.spaceshooter.ui.Bar;

import java.util.Random;

public class GameScreen implements Screen {

    private final boolean debugMode;

    private final SpaceShooter game;
    private final SpaceshooterAssetManager assetManager;
    private final Random random = new Random();
    private final KeyboardController keyboardController = new KeyboardController();
    private final AudioManager audioManager;
    private long lastAsteroidSpawn;
    private ParticleEffect effect;
    private ParticleEffect laserExplosion;
    private Score score;
    private GameOver gameOver;

    private OrthographicCamera camera;

    private Bar healthBar;
    private Bar energyBar;
    private int spawnCount = 0;


    private Engine engine;


    public GameScreen(final SpaceShooter game, AudioManager audioManager, final boolean debugMode) {

        this.debugMode = debugMode;
        this.game = game;
        this.assetManager = game.getAssetManager();

        Gdx.app.log("SpaceShooter", "creating things.");

        gameOver = new GameOver();
        this.audioManager = audioManager;


    }

    private void createPlayer() {

        Ship ship = new Ship(MathUtils.floor(SpaceShooter.SCREEN_WIDTH / 2f), MathUtils.floor(SpaceShooter.SCREEN_HEIGHT / 2f), 64, 64);
        TextureComponent textureComponent = new TextureComponent();

        textureComponent.region = new TextureRegion(assetManager.getShipTexture());

        ship.add(textureComponent);
        ship.add(new CollisionComponent(ship));
        ship.add(new DamageComponent(Integer.MAX_VALUE));

        engine.addEntity(ship);
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
        float width = assetManager.getEnemy(id).getRegionWidth();
        float height = assetManager.getEnemy(id).getRegionHeight();

        Entity enemy = new Entity();
        enemy.add(new HazardComponent(200));
        enemy.add(new PositionComponent(SpaceShooter.SCREEN_WIDTH + 10, MathUtils.floor(MathUtils.random(10, SpaceShooter.SCREEN_HEIGHT - height))));
        enemy.add(new SizeComponent(MathUtils.floor(width) - 10, MathUtils.floor(height) - 10));

        TextureComponent textureComponent = new TextureComponent();
        textureComponent.region = assetManager.getEnemy(id);

        enemy.add(textureComponent);
        enemy.add(new CollisionComponent(enemy));
        enemy.add(new AttributesComponent(10));
        enemy.add(new DamageComponent(10));
        Gdx.app.log("GameScreen", "Added enemy");
        engine.addEntity(enemy);
    }

    private void spawnAsteroid() {
        int id = random.nextInt(assetManager.getNumberOfAsteroids());
        final String regionName = assetManager.getAsteroid(id).name;
        int asteroidHealth = 100;
        if (regionName.contains("big")) {
            asteroidHealth = 7;
        } else if (regionName.contains("med")) {
            asteroidHealth = 5;
        } else if (regionName.contains("small")) {
            asteroidHealth = 3;
        } else if (regionName.contains("tiny")) {
            asteroidHealth = 1;
        }
        float width = assetManager.getAsteroid(id).getRegionWidth();
        float height = assetManager.getAsteroid(id).getRegionHeight();

        Entity asteroid = new Entity();
        asteroid.add(new PositionComponent(SpaceShooter.SCREEN_WIDTH + 10, MathUtils.floor(MathUtils.random(10, SpaceShooter.SCREEN_HEIGHT - height))));
        asteroid.add(new SizeComponent(MathUtils.floor(width), MathUtils.floor(height)));
        asteroid.add(new HazardComponent(200));
        TextureComponent textureComponent = new TextureComponent();
        textureComponent.region = assetManager.getAsteroid(id);
        asteroid.add(textureComponent);
        asteroid.add(new CollisionComponent(asteroid));
        asteroid.add(new AttributesComponent(asteroidHealth));
        asteroid.add(new DamageComponent(10));
        Gdx.app.log("GameScreen", "Added asteroid");
        engine.addEntity(asteroid);
    }

    @Override
    public void show() {
        effect = new ParticleEffect();
        effect.load(Gdx.files.internal("images/particles/meteor_explosion.p"), Gdx.files.internal("images/particles"));

        laserExplosion = new ParticleEffect();
        laserExplosion.load(Gdx.files.internal("images/particles/laser_explosion.p"), Gdx.files.internal("images/particles"));
        score = new Score();
        audioManager.playGameMusic();
        Gdx.input.setInputProcessor(keyboardController);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, SpaceShooter.SCREEN_WIDTH, SpaceShooter.SCREEN_HEIGHT);

        engine = new PooledEngine();
        engine.addSystem(new RenderSystem(game.batch, game.shapeRenderer, camera, assetManager, debugMode));
        engine.addSystem(new PlayerControlSystem(keyboardController, audioManager));
        engine.addSystem(new LaserSystem());
        engine.addSystem(new CollisionSystem());
        engine.addSystem(new HazardSystem());
        createPlayer();
        engine.addEntityListener(Family.all(HazardComponent.class).get(), new EntityListener() {
            @Override
            public void entityAdded(final Entity entity) {

            }

            @Override
            public void entityRemoved(final Entity entity) {
                if (entity.getComponent(AttributesComponent.class).isDead()) {
                    score.add(1);
                    audioManager.playExplosion();
                }
                PositionComponent position = entity.getComponent(PositionComponent.class);
                effect.setPosition(position.x, position.y);
                effect.start();
            }
        });
        engine.addEntityListener(Families.getPlayer(), new PlayerEntityListener() {
            @Override
            public void playerAdded(final Ship ship) {
                gameOver.show();
            }

            @Override
            public void playerRemoved(final Ship ship) {
                gameOver.show();
            }
        });
        engine.addEntityListener(Families.getLaser(), new EntityListener() {

            @Override
            public void entityAdded(final Entity entity) {

            }

            @Override
            public void entityRemoved(final Entity entity) {
                PositionComponent position = entity.getComponent(PositionComponent.class);
                laserExplosion.setPosition(position.x, position.y);
                laserExplosion.start();
            }
        });

        healthBar = new Bar(assetManager.getHealthBarLeft(), assetManager.getHealthBarMid(), assetManager.getHealthBarRight(), SpaceShooter.SCREEN_WIDTH - 220, SpaceShooter.SCREEN_HEIGHT - 40);
        energyBar = new Bar(assetManager.getEnergyBarLeft(), assetManager.getEnergyBarMid(), assetManager.getEnergyBarRight(), SpaceShooter.SCREEN_WIDTH - 220, SpaceShooter.SCREEN_HEIGHT - 24);
    }

    @Override
    public void render(final float delta) {
        Gdx.gl.glClearColor(0, 0, .2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (gameOver.isVisible()) {
            game.batch.begin();
            renderGameOver(delta);
            game.batch.end();
            renderObjects(delta);
            if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
                resetGame();
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                quitGame();
            }
        } else {
            engine.update(delta);
            updateObjects(delta);
            renderObjects(delta);
        }

    }

    private void renderObjects(final float delta) {
        game.batch.begin();
        score.render(game.batch, game.bundle);
        effect.draw(game.batch, delta);
        laserExplosion.draw(game.batch, delta);

        ImmutableArray<Entity> players = engine.getEntitiesFor(Families.getPlayer());
        if (players.size() > 0) {
            Entity player = players.get(0);
            AttributesComponent playerComponent = player.getComponent(AttributesComponent.class);
            float healthBarWidth = (float) playerComponent.getHealth() / Ship.MAX_HEALTH * 200f;
            healthBar.render(game.batch, healthBarWidth);

            float energyBarWidth = (float) playerComponent.getEnergy() / Ship.MAX_ENERGY * 200f;
            energyBar.render(game.batch, energyBarWidth);
        }
        game.batch.end();


    }

    private void updateObjects(final float delta) {
        if (TimeUtils.millis() - lastAsteroidSpawn > 500) {
            spawnHazard();
        }
    }

    private void renderGameOver(final float delta) {
        gameOver.render(game.batch, game.bundle, delta);
    }

    private void quitGame() {
        game.changeScreen(ScreenManager.MENU);
    }

    private void resetGame() {
        score.reset();

        createPlayer();
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
        resetGame();
    }

    @Override
    public void dispose() {
        assetManager.dispose();
        effect.dispose();
        laserExplosion.dispose();
    }
}
