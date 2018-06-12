package io.github.naxos84.spaceshooter.model;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class Asteroid {

    private static final int MAX_HEALTH = 100;
    private static final int MIN_HEALTH = 100;
    private int rotation;
    private float speed = 200;
    private Rectangle rect;
    private int currentHealth;

    public Asteroid(final float x, final float y, final float width, final float height, final int rotation) {
        this.rect = new Rectangle(x, y, width, height);
        this.rotation = rotation;
        this.currentHealth = MAX_HEALTH;
    }

    public void updatePosition(final float deltaTime) {
        rect.x -= speed * deltaTime;
    }

    public void setSpeed(final float speed) {
        this.speed = speed;
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

    public boolean overlaps(final Rectangle other) {
        return rect.overlaps(other);
    }

    public Rectangle getCollisionBox() {
        return rect;
    }

    public int getRotation() {
        return rotation;
    }

    public boolean isDead() {
        return rect.x + rect.width < 0 || currentHealth == 0;
    }
}
