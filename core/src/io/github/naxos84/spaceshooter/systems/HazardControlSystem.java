package io.github.naxos84.spaceshooter.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import io.github.naxos84.spaceshooter.components.MoverComponent;
import io.github.naxos84.spaceshooter.components.PositionComponent;

public class HazardControlSystem extends IteratingSystem {

    private ComponentMapper<MoverComponent> moverMapper;

    public HazardControlSystem() {
        super(Family.all(MoverComponent.class, PositionComponent.class).get());

        moverMapper = ComponentMapper.getFor(MoverComponent.class);
    }

    @Override
    protected void processEntity(final Entity entity, final float deltaTime) {
        MoverComponent component = entity.getComponent(MoverComponent.class);
        PositionComponent pos = entity.getComponent(PositionComponent.class);

        pos.x += component.speed * deltaTime;
        if (pos.x > 800 || pos.x < 0) {
            getEngine().removeEntity(entity);
        }
    }

    @Override
    public void update(final float deltaTime) {
        super.update(deltaTime);
    }
}
