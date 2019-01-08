package io.github.naxos84.spaceshooter.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import io.github.naxos84.spaceshooter.Families;
import io.github.naxos84.spaceshooter.components.*;


public class CollisionSystem extends EntitySystem {

    private Engine engine;

    private ImmutableArray<Entity> players;
    private ImmutableArray<Entity> asteroids;
    private ImmutableArray<Entity> enemies;
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
        asteroids = engine.getEntitiesFor(Families.getAsteroid());
        enemies = engine.getEntitiesFor(Families.getEnemy());
        lasers = engine.getEntitiesFor(Families.getLaser());
    }

    @Override
    public void update(final float deltaTime) {
        for (Entity player : players) {
            SizeComponent playerSize = sizeMapper.get(player);
            PositionComponent playerPosition = positionMapper.get(player);
            Rectangle playerRectangle = new Rectangle(playerPosition.x, playerPosition.y,  playerSize.getWidth(), playerSize.getHeight());
            for (Entity asteroid : asteroids) { //all asteroids
                SizeComponent asteroidSize = sizeMapper.get(asteroid);
                PositionComponent asteroidPosition = positionMapper.get(asteroid);
                Rectangle asteroidRectangle = new Rectangle(asteroidPosition.x, asteroidPosition.y, asteroidSize.getWidth(), asteroidSize.getHeight());
                if (playerRectangle.overlaps(asteroidRectangle)) {
                    collisionMapper.get(player).handleCollision(asteroid);
                    if (asteroid.getComponent(AttributesComponent.class).isDead()) {
                        Gdx.app.log(this.getClass().getName(), "Asteroid died by collision with player.");
                        engine.removeEntity(asteroid);
                    }
                }
            }
            for (Entity enemy : enemies) {
                SizeComponent enemySize = sizeMapper.get(enemy);
                PositionComponent enemyPosition = positionMapper.get(enemy);
                Rectangle enemyRectangle = new Rectangle(enemyPosition.x, enemyPosition.y, enemySize.getWidth(), enemySize.getHeight());
                if (playerRectangle.overlaps(enemyRectangle)) {
                    collisionMapper.get(player).handleCollision(enemy);
                    if (enemy.getComponent(AttributesComponent.class).isDead()) {
                        Gdx.app.log(this.getClass().getName(), "Enemy died by collision with player.");
                        engine.removeEntity(enemy);
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
            Rectangle playerRectangle = new Rectangle(playerPosition.x, playerPosition.y,  playerSize.getWidth(), playerSize.getHeight());
            for (Entity asteroid : asteroids) { //all asteroids
                SizeComponent asteroidSize = sizeMapper.get(asteroid);
                PositionComponent asteroidPosition = positionMapper.get(asteroid);
                Rectangle asteroidRectangle = new Rectangle(asteroidPosition.x, asteroidPosition.y, asteroidSize.getWidth(), asteroidSize.getHeight());
                if (playerRectangle.overlaps(asteroidRectangle)) {
                    collisionMapper.get(laser).handleCollision(asteroid);
                    collisionMapper.get(asteroid).handleCollision(laser);
                    if (laser.getComponent(AttributesComponent.class).isDead()) {
                        engine.removeEntity(laser);
                    }
                    if (asteroid.getComponent(AttributesComponent.class).isDead()) {
                        engine.removeEntity(laser);
                    }
                }
            }
            for (Entity enemy : enemies) {
                SizeComponent enemySize = sizeMapper.get(enemy);
                PositionComponent enemyPosition = positionMapper.get(enemy);
                Rectangle enemyRectangle = new Rectangle(enemyPosition.x, enemyPosition.y, enemySize.getWidth(), enemySize.getHeight());
                if (playerRectangle.overlaps(enemyRectangle)) {
                    collisionMapper.get(laser).handleCollision(enemy);
                    collisionMapper.get(enemy).handleCollision(laser);
                    if (laser.getComponent(AttributesComponent.class).isDead()) {
                        engine.removeEntity(laser);
                    }
                    if (enemy.getComponent(AttributesComponent.class).isDead()) {
                        engine.removeEntity(laser);
                    }
                }
            }
        }
    }
}
