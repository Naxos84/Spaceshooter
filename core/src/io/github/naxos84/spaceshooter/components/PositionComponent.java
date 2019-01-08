package io.github.naxos84.spaceshooter.components;

import com.badlogic.ashley.core.Component;

public class PositionComponent implements Component {

    public float x;
    public float y;
    public boolean isHidden;

    public PositionComponent(final float x, final float y) {
        this.x = x;
        this.y = y;
    }


}
