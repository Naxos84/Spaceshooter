package io.github.naxos84.spaceshooter.components;

import com.badlogic.ashley.core.Component;

public class HealthComponent implements Component {

    private final int maxHealth;
    private int health;

    public HealthComponent(final int health) {
        this.health = health;
        this.maxHealth = health;
    }

    public void reduce(final int health) {
        this.health -= health;
    }

    public boolean isDead() {
        return health <= 0;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }
}
