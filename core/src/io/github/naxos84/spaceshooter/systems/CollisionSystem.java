package io.github.naxos84.spaceshooter.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Rectangle;
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
        players = engine.getEntitiesFor(Family.all(PlayerComponent.class, CollisionComponent.class).get());
        asteroids = engine.getEntitiesFor(Family.all(AsteroidsComponent.class, PositionComponent.class, SizeComponent.class, CollisionComponent.class).get());
        enemies = engine.getEntitiesFor(Family.all(EnemyComponent.class, PositionComponent.class, SizeComponent.class, CollisionComponent.class).get());
        lasers = engine.getEntitiesFor(Family.all(LaserComponent.class,PositionComponent.class, SizeComponent.class, CollisionComponent.class).get());
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
                    collisionMapper.get(player).collisionEntity = asteroid;
                }
            }
            for (Entity enemy : enemies) {
                SizeComponent enemySize = sizeMapper.get(enemy);
                PositionComponent enemyPosition = positionMapper.get(enemy);
                Rectangle enemyRectangle = new Rectangle(enemyPosition.x, enemyPosition.y, enemySize.getWidth(), enemySize.getHeight());
                if (playerRectangle.overlaps(enemyRectangle)) {
                    collisionMapper.get(player).collisionEntity = enemy;
                }
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
                    collisionMapper.get(laser).collisionEntity = asteroid;
                    collisionMapper.get(asteroid).collisionEntity = laser;
                }
            }
            for (Entity enemy : enemies) {
                SizeComponent enemySize = sizeMapper.get(enemy);
                PositionComponent enemyPosition = positionMapper.get(enemy);
                Rectangle enemyRectangle = new Rectangle(enemyPosition.x, enemyPosition.y, enemySize.getWidth(), enemySize.getHeight());
                if (playerRectangle.overlaps(enemyRectangle)) {
                    collisionMapper.get(laser).collisionEntity = enemy;
                    collisionMapper.get(enemy).collisionEntity = laser;
                }
            }
        }
    }
}
