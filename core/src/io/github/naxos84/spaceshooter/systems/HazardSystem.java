package io.github.naxos84.spaceshooter.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import io.github.naxos84.spaceshooter.SpaceShooter;
import io.github.naxos84.spaceshooter.components.CollisionComponent;
import io.github.naxos84.spaceshooter.components.HazardComponent;
import io.github.naxos84.spaceshooter.components.PositionComponent;
import io.github.naxos84.spaceshooter.components.SizeComponent;

public class HazardSystem extends IteratingSystem {

    private ComponentMapper<HazardComponent> asteroidsMapper;
    private ComponentMapper<PositionComponent> positionMapper;
    private ComponentMapper<SizeComponent> sizeMapper;

    public HazardSystem() {
        super(Family.all(HazardComponent.class, PositionComponent.class, SizeComponent.class, CollisionComponent.class).get());

        asteroidsMapper = ComponentMapper.getFor(HazardComponent.class);
        positionMapper = ComponentMapper.getFor(PositionComponent.class);
        sizeMapper = ComponentMapper.getFor(SizeComponent.class);
    }

    @Override
    protected void processEntity(final Entity entity, final float deltaTime) {
        HazardComponent component = asteroidsMapper.get(entity);
        PositionComponent position = positionMapper.get(entity);
        SizeComponent size = sizeMapper.get(entity);

        position.x += component.getSpeed() * deltaTime;
        if (position.x > SpaceShooter.SCREEN_WIDTH + 100 || position.x < 0 - size.getWidth()) {
            getEngine().removeEntity(entity);
            Gdx.app.log(this.getClass().getName(), "Hazard entity left screen --> removed.");
        }
    }

    @Override
    public void update(final float deltaTime) {
        super.update(deltaTime);
    }
}
