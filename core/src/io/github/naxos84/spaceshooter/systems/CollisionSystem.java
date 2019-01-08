package io.github.naxos84.spaceshooter.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import io.github.naxos84.spaceshooter.Families;
import io.github.naxos84.spaceshooter.components.AttributesComponent;
import io.github.naxos84.spaceshooter.components.CollisionComponent;
import io.github.naxos84.spaceshooter.components.PositionComponent;
import io.github.naxos84.spaceshooter.components.SizeComponent;


public class CollisionSystem extends EntitySystem {

    private Engine engine;

    private ImmutableArray<Entity> players;
    private ImmutableArray<Entity> hazards;
    private ImmutableArray<Entity> lasers;

    private ComponentMapper<SizeComponent> sizeMapper;
    private ComponentMapper<PositionComponent> positionMapper;
    private ComponentMapper<CollisionComponent> collisionMapper;

    public CollisionSystem() {
        sizeMapper = ComponentMapper.getFor(SizeComponent.class);
        positionMapper = ComponentMapper.getFor(PositionComponent.class);
        collisionMapper = ComponentMapper.getFor(CollisionComponent.class);
    }

    @Override
    public void addedToEngine(final Engine engine) {
        this.engine = engine;
        players = engine.getEntitiesFor(Families.getPlayer());
        hazards = engine.getEntitiesFor(Families.getHazards());
        lasers = engine.getEntitiesFor(Families.getLaser());
    }

    @Override
    public void update(final float deltaTime) {
        for (Entity player : players) {
            SizeComponent playerSize = sizeMapper.get(player);
            PositionComponent playerPosition = positionMapper.get(player);
            Rectangle playerRectangle = new Rectangle(playerPosition.x, playerPosition.y, playerSize.getWidth(), playerSize.getHeight());
            for (Entity asteroid : hazards) { //all hazards
                SizeComponent asteroidSize = sizeMapper.get(asteroid);
                PositionComponent asteroidPosition = positionMapper.get(asteroid);
                Rectangle asteroidRectangle = new Rectangle(asteroidPosition.x, asteroidPosition.y, asteroidSize.getWidth(), asteroidSize.getHeight());
                if (playerRectangle.overlaps(asteroidRectangle)) {
                    collisionMapper.get(player).handleCollision(asteroid);
                    if (asteroid.getComponent(AttributesComponent.class).isDead()) {
                        Gdx.app.log(this.getClass().getName(), "Hazard died by collision with player.");
                        engine.removeEntity(asteroid);
                    }
                }
            }
            if (player.getComponent(AttributesComponent.class).isDead()) {
                engine.removeEntity(player);
            }
        }
        for (Entity laser : lasers) {
            SizeComponent playerSize = sizeMapper.get(laser);
            PositionComponent playerPosition = positionMapper.get(laser);
            Rectangle playerRectangle = new Rectangle(playerPosition.x, playerPosition.y, playerSize.getWidth(), playerSize.getHeight());
            for (Entity hazard : hazards) { //all hazards
                SizeComponent asteroidSize = sizeMapper.get(hazard);
                PositionComponent asteroidPosition = positionMapper.get(hazard);
                Rectangle asteroidRectangle = new Rectangle(asteroidPosition.x, asteroidPosition.y, asteroidSize.getWidth(), asteroidSize.getHeight());
                if (playerRectangle.overlaps(asteroidRectangle)) {
                    collisionMapper.get(laser).handleCollision(hazard);
                    collisionMapper.get(hazard).handleCollision(laser);
                    if (laser.getComponent(AttributesComponent.class).isDead()) {
                        engine.removeEntity(laser);
                    }
                    if (hazard.getComponent(AttributesComponent.class).isDead()) {
                        engine.removeEntity(hazard);
                    }
                }
            }
        }
    }
}
