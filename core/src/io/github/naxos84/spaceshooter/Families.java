package io.github.naxos84.spaceshooter;

import com.badlogic.ashley.core.Family;
import io.github.naxos84.spaceshooter.components.*;

public class Families {

    private Families() {
    }

    public static Family getPlayer() {
        return Family.all(AttributesComponent.class).exclude(HazardComponent.class, HazardComponent.class, LaserComponent.class).get();
    }

    public static Family getHazards() {
        return Family.all(HazardComponent.class, PositionComponent.class, SizeComponent.class, CollisionComponent.class).get();
    }

    public static Family getLaser() {
        return Family.all(LaserComponent.class, PositionComponent.class, SizeComponent.class, CollisionComponent.class).get();
    }
}
