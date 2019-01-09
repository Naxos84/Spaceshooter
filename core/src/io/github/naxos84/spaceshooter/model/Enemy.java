package io.github.naxos84.spaceshooter.model;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class Enemy {


    private final int id;
    private int rotation;
    private float speed = 200;
    private Rectangle rect;
    private float currentHealth = 0;
    private float maxHealth = 0;

    public Enemy(final int id, final float x, final float y, final float width, final float height, final int rotation) {
        this.rect = new Rectangle(x, y, width, height);
        this.rotation = rotation;
        this.id = id;
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

    public float getCurrentHealth() {
        return currentHealth;
    }

    public void setHealth(final int health) {
        if (health < 0) {
            this.currentHealth = 0;
        } else {
            this.currentHealth = health;
        }
        this.maxHealth = currentHealth;
    }

    public void reduceHealth(int health) {
        this.currentHealth -= health;
        this.currentHealth = MathUtils.clamp(this.currentHealth, 0, maxHealth);
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

    public int getId() {
        return id;
    }

    public float getMaxHealth() {
        return maxHealth;
    }

    public boolean isFullLife() {
        return currentHealth == maxHealth;
    }
}
