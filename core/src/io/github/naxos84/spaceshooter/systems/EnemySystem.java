package io.github.naxos84.spaceshooter.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import io.github.naxos84.spaceshooter.SpaceShooter;
import io.github.naxos84.spaceshooter.components.*;
import io.github.naxos84.spaceshooter.manager.AudioManager;

public class EnemySystem extends IteratingSystem {

    private ComponentMapper<EnemyComponent> asteroidsMapper;
    private ComponentMapper<PositionComponent> positionMapper;
    private ComponentMapper<SizeComponent> sizeMapper;
    private ComponentMapper<CollisionComponent> collisionMapper;
    private final AudioManager audioManager;

    public EnemySystem(final AudioManager audioManager) {
        super(Family.all(EnemyComponent.class, PositionComponent.class, SizeComponent.class, CollisionComponent.class).get());

        this.audioManager = audioManager;
        asteroidsMapper = ComponentMapper.getFor(EnemyComponent.class);
        positionMapper = ComponentMapper.getFor(PositionComponent.class);
        sizeMapper = ComponentMapper.getFor(SizeComponent.class);
        collisionMapper = ComponentMapper.getFor(CollisionComponent.class);
    }

    @Override
    protected void processEntity(final Entity entity, final float deltaTime) {
        EnemyComponent component = asteroidsMapper.get(entity);
        PositionComponent position = positionMapper.get(entity);
        SizeComponent size = sizeMapper.get(entity);

        position.x += component.getSpeed() * deltaTime;
        if (position.x > SpaceShooter.SCREEN_WIDTH + 100 || position.x < 0 - size.getWidth()) {
            getEngine().removeEntity(entity);
            Gdx.app.log("LaserSystem", "entity removed.");
        }
//        CollisionComponent collisionComponent = collisionMapper.get(entity);
//        if (collisionComponent.collisionEntity != null) {
//            Gdx.app.log("AsteroidsSystem", "Collision detected.");
//            LaserComponent lc = collisionComponent.collisionEntity.getComponent(LaserComponent.class);
//            if (lc != null) {
//                getEngine().removeEntity(entity);
//                getEngine().removeEntity(collisionComponent.collisionEntity);
//                audioManager.playExplosion();
//            }
//            collisionComponent.collisionEntity = null;
//        }
    }

    @Override
    public void update(final float deltaTime) {
        super.update(deltaTime);
    }
}