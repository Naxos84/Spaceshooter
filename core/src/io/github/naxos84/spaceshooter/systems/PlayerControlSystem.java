package io.github.naxos84.spaceshooter.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;
import io.github.naxos84.spaceshooter.SpaceShooter;
import io.github.naxos84.spaceshooter.components.*;
import io.github.naxos84.spaceshooter.controller.KeyboardController;
import io.github.naxos84.spaceshooter.manager.AudioManager;

public class PlayerControlSystem extends IteratingSystem {

    private ComponentMapper<PositionComponent> positionMapper;
    private ComponentMapper<SizeComponent> sizeMapper;
    private ComponentMapper<CollisionComponent> collisionMapper;
    private ComponentMapper<AttributesComponent> playerMapper;
    private final KeyboardController keyboardController;
    private float rateOfFire;
    private long lastLaserSpawn;
    private final AudioManager audioManager;

    public PlayerControlSystem(final KeyboardController keyboardController, final AudioManager audioManager) {
        super(Family.all(PositionComponent.class, SizeComponent.class, AttributesComponent.class).exclude(AsteroidsComponent.class, EnemyComponent.class, LaserComponent.class).get());
        this.keyboardController = keyboardController;
        this.audioManager = audioManager;

        positionMapper = ComponentMapper.getFor(PositionComponent.class);
        sizeMapper = ComponentMapper.getFor(SizeComponent.class);
        collisionMapper = ComponentMapper.getFor(CollisionComponent.class);
        playerMapper = ComponentMapper.getFor(AttributesComponent.class);
        rateOfFire = 10;
        lastLaserSpawn = TimeUtils.millis();
    }

    @Override
    protected void processEntity(final Entity entity, final float deltaTime) {
        PositionComponent position = positionMapper.get(entity);
        if (position != null) {
            if (keyboardController.isLeftPressed()) {
                position.x -= 200 * deltaTime;

            }
            if (keyboardController.isRightPressed()) {
                position.x += 200 * deltaTime;
            }
            if (keyboardController.isUpPressed()) {
                position.y += 200 * deltaTime;
            }
            if (keyboardController.isDownPressed()) {
                position.y -= 200 * deltaTime;
            }
            position.x = MathUtils.clamp(position.x, 0, SpaceShooter.SCREEN_WIDTH - 64);
            position.y = MathUtils.clamp(position.y, 0, SpaceShooter.SCREEN_HEIGHT - 64);
            if (keyboardController.isSpacePressed()) {
                if (TimeUtils.millis() - lastLaserSpawn >  1000 / rateOfFire && playerMapper.get(entity).hasEnergy(10)) {
                    Entity laser = new Entity();
                    SizeComponent size = sizeMapper.get(entity);
                    PositionComponent positionComponent = new PositionComponent(position.x + size.getWidth(), position.y + size.getHeight() / 2 + 3);

                    laser.add(positionComponent);
                    TextureComponent textureComponent = new TextureComponent();
                    textureComponent.region = new TextureRegion(new Texture(Gdx.files.internal("images/lasers/laserBlue01.png")));
                    laser.add(textureComponent);
                    laser.add(new LaserComponent(600));
                    laser.add(new CollisionComponent(laser));
                    laser.add(new SizeComponent(29, 6));
                    laser.add(new DamageComponent(1));
                    laser.add(new AttributesComponent(1));
                    getEngine().addEntity(laser);
                    lastLaserSpawn = TimeUtils.millis();
                    audioManager.playLaserSound();
                    playerMapper.get(entity).reduceEnergy(10);
                }
            }
        } else {
            Gdx.app.log("ERROR", "No position available.");
        }

//        CollisionComponent collisionComponent = collisionMapper.get(entity);
//        if (collisionComponent.collisionEntity != null) {
//            Gdx.app.log("PlayerControlSystem", "Player health: " + playerMapper.get(entity).getHealth());
//            int maxHealth = playerMapper.get(collisionComponent.collisionEntity).getMaxHealth();
//            playerMapper.get(entity).reduceHealth(maxHealth);
//            Gdx.app.log("PlayerControlSystem", "Player collided.");
//            getEngine().removeEntity(collisionComponent.collisionEntity);
//            collisionComponent.collisionEntity = null;
//        }
        if (playerMapper.get(entity).isDead()) {
            getEngine().removeEntity(entity);
        }
        playerMapper.get(entity).updateEnergy(deltaTime);
    }

    @Override
    public void update(final float deltaTime) {
        super.update(deltaTime);
    }
}
