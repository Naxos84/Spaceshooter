package io.github.naxos84.spaceshooter.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import io.github.naxos84.spaceshooter.SpaceShooter;
import io.github.naxos84.spaceshooter.components.*;
import io.github.naxos84.spaceshooter.manager.AudioManager;

public class AsteroidsSystem extends IteratingSystem {

    private ComponentMapper<AsteroidsComponent> asteroidsMapper;
    private ComponentMapper<PositionComponent> positionMapper;
    private ComponentMapper<SizeComponent> sizeMapper;

    public AsteroidsSystem(final AudioManager audioManager) {
        super(Family.all(AsteroidsComponent.class, PositionComponent.class, SizeComponent.class, CollisionComponent.class).get());

        asteroidsMapper = ComponentMapper.getFor(AsteroidsComponent.class);
        positionMapper = ComponentMapper.getFor(PositionComponent.class);
        sizeMapper = ComponentMapper.getFor(SizeComponent.class);
    }

    @Override
    protected void processEntity(final Entity entity, final float deltaTime) {
        AsteroidsComponent component = asteroidsMapper.get(entity);
        PositionComponent position = positionMapper.get(entity);
        SizeComponent size = sizeMapper.get(entity);

        position.x += component.getSpeed() * deltaTime;
        if (position.x > SpaceShooter.SCREEN_WIDTH + 100 || position.x < 0 - size.getWidth()) {
            getEngine().removeEntity(entity);
            Gdx.app.log(this.getClass().getName(), "asteroid entity left screen --> removed.");
        }
    }

    @Override
    public void update(final float deltaTime) {
        super.update(deltaTime);
    }
}
