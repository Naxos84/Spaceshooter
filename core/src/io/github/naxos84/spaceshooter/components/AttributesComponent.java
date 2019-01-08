package io.github.naxos84.spaceshooter.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.MathUtils;

public class AttributesComponent implements Component {

    private final int maxHealth;
    private int health;

    private final int maxEnergy;
    private int energy;

    private float energyTimer;

    public AttributesComponent(final int health, final int energy) {
        this.health = health;
        this.maxHealth = health;
        maxEnergy = energy;
        this.energy = energy;
    }

    public AttributesComponent(final int health) {
        this(health, 0);
    }

    public void reduceHealth(final int health) {
        this.health -= health;
    }

    public void reduceEnergy(final int energy) {
        this.energy -= energy;
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

    public void updateEnergy(final float deltaTime) {
        energyTimer += deltaTime;
        if (energyTimer >= .1f) {
            energy +=5;
            energy = MathUtils.clamp(energy, 0, maxEnergy);
            energyTimer -=.1f;
        }
    }

    public boolean hasEnergy(final int energy) {
        return this.energy >= energy;
    }

    public int getEnergy() {
        return energy;
    }
}
