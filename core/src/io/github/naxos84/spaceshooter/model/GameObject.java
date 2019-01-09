package io.github.naxos84.spaceshooter.model;

import com.badlogic.ashley.core.Entity;
import io.github.naxos84.spaceshooter.components.PositionComponent;
import io.github.naxos84.spaceshooter.components.SizeComponent;

public class GameObject extends Entity {

    public GameObject() {
        this(0, 0, 1, 1);
    }

    public GameObject(float x, float y, int width, int height) {
        add(new PositionComponent(x, y));
        add(new SizeComponent(width, height));
    }

    public PositionComponent getPosition() {
        return getComponent(PositionComponent.class);
    }

    public SizeComponent getSize() {
        return getComponent(SizeComponent.class);
    }
}
