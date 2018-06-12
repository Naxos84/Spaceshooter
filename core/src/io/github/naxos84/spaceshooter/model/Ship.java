package io.github.naxos84.spaceshooter.model;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import io.github.naxos84.spaceshooter.SpaceShooter;

public class Ship {

    public static final int MAX_HEALTH = 100;
    private static final int MIN_HEALTH = 0;

    public static final int MAX_ENERGY = 300;
    private static final int MIN_ENERGY = 0;

    private float hSpeed = 200;
    private float vSpeed = 200;
    private Rectangle rect;
    private int currentHealth;
    private int energy;

    public Ship(final float x, final float y, final float height, final float width) {
        this.rect = new Rectangle(x, y, width, height);
        currentHealth = MAX_HEALTH;
        energy = MAX_ENERGY;
    }


    public void updatePosition() {
        rect.x = MathUtils.clamp(rect.x, 0, SpaceShooter.SCREEN_WIDTH - rect.width);
        rect.y = MathUtils.clamp(rect.y, 0, SpaceShooter.HEIGHT - rect.height);
    }

    public void moveLeft(final float delta) {
        rect.x -= hSpeed * delta;
    }

    public void moveRight(final float delta) {
        rect.x += hSpeed * delta;
    }

    public void moveUp(final float delta) {
        rect.y += vSpeed * delta;
    }

    public void moveDown(final float delta) {
        rect.y -= vSpeed * delta;
    }

    public float getX() {
        return rect.x;
    }

    public float getY() {
        return rect.y;
    }

    public float getWidth() {
        return rect.width;
    }

    public float getHeight() {
        return rect.height;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public void addHealth(final int health) {
        this.currentHealth += health;
        this.currentHealth = MathUtils.clamp(this.currentHealth, MIN_HEALTH, MAX_HEALTH);
    }

    public void reduceHealth(int health) {
        this.currentHealth -= health;
        this.currentHealth = MathUtils.clamp(this.currentHealth, MIN_HEALTH, MAX_HEALTH);
    }

    public boolean isDead() {
        return currentHealth == 0;
    }

    /**
     * Convenient method to !{@link #isDead()}
     * @return wether the ship is still alive
     */
    public boolean isAlive(){
        return !isDead();
    }

    public int getCurrentEnergy() {
        return energy;
    }

    public void addEnergy(final int energy) {
        this.energy += energy;
        this.energy = MathUtils.clamp(this.energy, MIN_ENERGY, MAX_ENERGY);
    }

    public void reduceEnergy(int energy) {
        this.energy -= energy;
        this.energy = MathUtils.clamp(this.energy, MIN_ENERGY, MAX_ENERGY);
    }

    public boolean overlaps(final Rectangle other) {
        return rect.overlaps(other);
    }

    public Rectangle getCollisionBox() {
        return rect;
    }

    public void setPosition(final int x, final int y) {
        this.rect.x = x;
        this.rect.y = y;
    }
}
