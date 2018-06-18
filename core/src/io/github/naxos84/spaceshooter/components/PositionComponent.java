package io.github.naxos84.spaceshooter.components;

import com.badlogic.ashley.core.Component;

public class PositionComponent implements Component {

    public int x;
    public int y;
    public boolean isHidden;

    public PositionComponent(final int x, final int y) {
        this.x = x;
        this.y = y;
    }


}
