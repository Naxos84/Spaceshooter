package io.github.naxos84.spaceshooter.systems;

import com.badlogic.ashley.core.Component;
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

import javax.xml.soap.Text;

public class PlayerControlSystem extends IteratingSystem {

    private ComponentMapper<PositionComponent> positionMapper;
    private ComponentMapper<SizeComponent> sizeMapper;
    private ComponentMapper<CollisionComponent> collisionMapper;
    private ComponentMapper<HealthComponent> healthMapper;
    private final KeyboardController keyboardController;
    private float rateOfFire;
    private long lastLaserSpawn;
    private final AudioManager audioManager;

    public PlayerControlSystem(final KeyboardController keyboardController, final AudioManager audioManager) {
        super(Family.all(PositionComponent.class, SizeComponent.class, PlayerComponent.class, HealthComponent.class).get());
        this.keyboardController = keyboardController;
        this.audioManager = audioManager;

        positionMapper = ComponentMapper.getFor(PositionComponent.class);
        sizeMapper = ComponentMapper.getFor(SizeComponent.class);
        collisionMapper = ComponentMapper.getFor(CollisionComponent.class);
        healthMapper = ComponentMapper.getFor(HealthComponent.class);
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
                if (TimeUtils.millis() - lastLaserSpawn >  1000 / rateOfFire) {
                    Entity laser = new Entity();
                    SizeComponent size = sizeMapper.get(entity);
                    PositionComponent positionComponent = new PositionComponent(position.x + size.getWidth(), position.y + size.getHeight() / 2 + 3);

                    laser.add(positionComponent);
                    TextureComponent textureComponent = new TextureComponent();
                    textureComponent.region = new TextureRegion(new Texture(Gdx.files.internal("images/lasers/laserBlue01.png")));
                    laser.add(textureComponent);
                    laser.add(new LaserComponent(600));
                    laser.add(new CollisionComponent());
                    laser.add(new SizeComponent(29, 6));
                    getEngine().addEntity(laser);
                    lastLaserSpawn = TimeUtils.millis();
                    audioManager.playLaserSound();
                }
            }
        } else {
            Gdx.app.log("ERROR", "No position available.");
        }

        CollisionComponent collisionComponent = collisionMapper.get(entity);
        if (collisionComponent.collisionEntity != null) {
            Gdx.app.log("PlayerControlSystem", "Player health: " + healthMapper.get(entity).getHealth());
            int maxHealth = healthMapper.get(collisionComponent.collisionEntity).getMaxHealth();
            healthMapper.get(entity).reduce(maxHealth);
            Gdx.app.log("PlayerControlSystem", "Player collided.");
            getEngine().removeEntity(collisionComponent.collisionEntity);
            collisionComponent.collisionEntity = null;
        }
        if (healthMapper.get(entity).isDead()) {
            getEngine().removeEntity(entity);
        }
    }

    @Override
    public void update(final float deltaTime) {
        super.update(deltaTime);
    }
}
