package io.github.naxos84.spaceshooter.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.MathUtils;

public class AttributesComponent implements Component {

    private final float maxHealth;
    private final float maxEnergy;
    private float health;
    private float energy;

    private float energyTimer;

    public AttributesComponent(final float health, final float energy, final float maxHealth, final float maxEnergy) {
        this.health = health;
        this.maxHealth = maxHealth;
        this.maxEnergy = maxEnergy;
        this.energy = energy;
    }

    public AttributesComponent(final int health) {
        this(health, 0, health, 0);
    }

    public void reduceHealth(final float health) {
        this.health -= health;
    }

    public void reduceEnergy(final float energy) {
        this.energy -= energy;
    }

    public boolean isDead() {
        return health <= 0;
    }

    public float getHealth() {
        return health;
    }

    public float getMaxHealth() {
        return maxHealth;
    }

    public void updateEnergy(final float deltaTime) {
        energyTimer += deltaTime;
        if (energyTimer >= .1f) {
            energy += 5;
            energy = MathUtils.clamp(energy, 0, maxEnergy);
            energyTimer -= .1f;
        }
    }

    public boolean hasEnergy(final float energy) {
        return this.energy >= energy;
    }

    public float getEnergy() {
        return energy;
    }
}
