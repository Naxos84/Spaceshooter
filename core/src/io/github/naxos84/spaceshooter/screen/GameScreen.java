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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import io.github.naxos84.spaceshooter.SpaceShooter;
import io.github.naxos84.spaceshooter.components.*;
import io.github.naxos84.spaceshooter.controller.KeyboardController;
import io.github.naxos84.spaceshooter.manager.AudioManager;
import io.github.naxos84.spaceshooter.manager.ScreenManager;
import io.github.naxos84.spaceshooter.manager.SpaceshooterAssetManager;
import io.github.naxos84.spaceshooter.model.*;
import io.github.naxos84.spaceshooter.overlay.GameOver;
import io.github.naxos84.spaceshooter.systems.*;

import java.util.Random;

public class GameScreen implements Screen {

    private final SpaceShooter game;
    private final SpaceshooterAssetManager assetManager;
    private final Random random = new Random();
    private final KeyboardController keyboardController = new KeyboardController();
    private final AudioManager audioManager;
    private Ship ship;
    private Array<Asteroid> asteroids;
    private long lastAsteroidSpawn;
    private Array<Laser> lasers;
    private Array<Enemy> enemies;
    private ParticleEffect effect;
    private Score score;
    private float energyTimer = 0f;
    private GameOver gameOver;
    private TextureRegion healthBarLeft;
    private TextureRegion healthBarMid;
    private TextureRegion healthBarRight;
    private TextureRegion energyBarLeft;
    private TextureRegion energyBarMid;
    private TextureRegion energyBarRight;
    private int spawnCount = 0;


    private Engine engine;


    public GameScreen(final SpaceShooter game, AudioManager audioManager, final boolean debugMode) {
        this.game = game;
        this.assetManager = game.getAssetManager();

        Gdx.app.log("SpaceShooter", "creating things.");

        ship = new Ship(800 / 2 - 64 / 2, 600 / 2 - 64 / 2, 64, 64);

        asteroids = new Array<>();
        lasers = new Array<>();
        enemies = new Array<>();

        gameOver = new GameOver();
        this.audioManager = audioManager;


    }

    private void createPlayer() {
        Entity entity = new Entity();
        PositionComponent positionComponent = new PositionComponent(MathUtils.floor(SpaceShooter.SCREEN_WIDTH / 2), MathUtils.floor(SpaceShooter.SCREEN_HEIGHT / 2));
        TextureComponent textureComponent = new TextureComponent();

        textureComponent.region = new TextureRegion(assetManager.getShipTexture());

        entity.add(positionComponent);
        entity.add(textureComponent);
        entity.add(new AttributesComponent(100, 100));
        entity.add(new SizeComponent(64, 64));
        entity.add(new CollisionComponent(entity));
        entity.add(new DamageComponent(Integer.MAX_VALUE));

        engine.addEntity(entity);
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
        enemy.add(new EnemyComponent(200));
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
        asteroid.add(new AsteroidsComponent(200));
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
        effect.load(Gdx.files.internal("images/particles/meteor_explosion.p"), Gdx.files.internal("images/meteors"));
        score = new Score();
        audioManager.playGameMusic();
        Gdx.input.setInputProcessor(keyboardController);
        engine = new PooledEngine();
        engine.addSystem(new RenderSystem(game.batch, game.shapeRenderer));
        engine.addSystem(new PlayerControlSystem(keyboardController, audioManager));
        engine.addSystem(new LaserSystem());
        engine.addSystem(new CollisionSystem());
        AsteroidsSystem asteroidsSystem = new AsteroidsSystem(audioManager);
        engine.addSystem(asteroidsSystem);
        engine.addSystem(new EnemySystem(audioManager));
        createPlayer();
        engine.addEntityListener(Family.all(AsteroidsComponent.class).get(), new EntityListener() {
            @Override
            public void entityAdded(final Entity entity) {

            }

            @Override
            public void entityRemoved(final Entity entity) {
                if (entity.getComponent(AttributesComponent.class).isDead()) {
                    score.add(1);
                }
                PositionComponent position = entity.getComponent(PositionComponent.class);
                effect.setPosition(position.x, position.y);
                effect.start();
            }
        });
        engine.addEntityListener(Family.all(EnemyComponent.class).get(), new EntityListener() {
            @Override
            public void entityAdded(final Entity entity) {

            }

            @Override
            public void entityRemoved(final Entity entity) {
                if (entity.getComponent(AttributesComponent.class).isDead()) {
                    score.add(1);
                }
                PositionComponent position = entity.getComponent(PositionComponent.class);
                effect.setPosition(position.x, position.y);
                effect.start();
            }
        });
        engine.addEntityListener(Family.all(AttributesComponent.class).exclude(AsteroidsComponent.class, EnemyComponent.class, LaserComponent.class).get(), new EntityListener() {
            @Override
            public void entityAdded(final Entity entity) {
                gameOver.hide();
            }

            @Override
            public void entityRemoved(final Entity entity) {
                gameOver.show();
            }
        });

        healthBarLeft = assetManager.getHealthBarLeft();
        healthBarMid = assetManager.getHealthBarMid();
        healthBarRight = assetManager.getHealthBarRight();
        energyBarLeft = assetManager.getEnergyBarLeft();
        energyBarMid = assetManager.getEnergyBarMid();
        energyBarRight = assetManager.getEnergyBarRight();
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

        ImmutableArray<Entity> players = engine.getEntitiesFor(Family.all(AttributesComponent.class).exclude(AsteroidsComponent.class, EnemyComponent.class, LaserComponent.class).get());
        if (players.size() > 0) {
            Entity player = players.get(0);
            AttributesComponent playerComponent = player.getComponent(AttributesComponent.class);
            float healthBarWidth = (float) playerComponent.getHealth() / Ship.MAX_HEALTH * 200f;
            game.batch.draw(healthBarLeft, SpaceShooter.SCREEN_WIDTH - 220, SpaceShooter.SCREEN_HEIGHT - 40, 6, 15);
            game.batch.draw(healthBarMid, SpaceShooter.SCREEN_WIDTH - 214, SpaceShooter.SCREEN_HEIGHT - 40, healthBarWidth, 15);
            game.batch.draw(healthBarRight, SpaceShooter.SCREEN_WIDTH - 214 + healthBarWidth, SpaceShooter.SCREEN_HEIGHT - 40, 6, 15);

            float energyBarWidth = (float) playerComponent.getEnergy() / Ship.MAX_ENERGY * 200f;
            game.batch.draw(energyBarLeft, SpaceShooter.SCREEN_WIDTH - 220, SpaceShooter.SCREEN_HEIGHT - 24, 6, 15);
            game.batch.draw(energyBarMid, SpaceShooter.SCREEN_WIDTH - 214, SpaceShooter.SCREEN_HEIGHT - 24, energyBarWidth, 15);
            game.batch.draw(energyBarRight, SpaceShooter.SCREEN_WIDTH - 214 + energyBarWidth, SpaceShooter.SCREEN_HEIGHT - 24, 6, 15);
        }
        game.batch.end();


    }

    private void updateObjects(final float delta) {
        energyTimer += delta;
        if (energyTimer >= .1f) {
            energyTimer -= .1f;
        }


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
        ship.addHealth(Ship.MAX_HEALTH);
        ship.addEnergy(Ship.MAX_ENERGY);
        ship.setPosition(800 / 2 - 64 / 2, 600 / 2 - 64 / 2);
        asteroids.clear();
        lasers.clear();
        score.reset();
        enemies.clear();

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
    }

    @Override
    public void dispose() {
        assetManager.dispose();
        effect.dispose();
    }
}
