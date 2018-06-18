package io.github.naxos84.spaceshooter.components;

import com.badlogic.ashley.core.Component;

class MoverComponent implements Component {

    private float speed;

    public MoverComponent(final float speed) {
        this.speed = speed;
    }

    public float getSpeed() {
        return speed;
    }
}
