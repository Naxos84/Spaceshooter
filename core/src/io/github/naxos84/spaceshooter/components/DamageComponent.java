package io.github.naxos84.spaceshooter.components;

import com.badlogic.ashley.core.Component;

public class DamageComponent implements Component {

    private int damage;

    public DamageComponent(final int damage) {
        this.damage = damage;
    }

    public int getDamage() {
        return damage;
    }
}
