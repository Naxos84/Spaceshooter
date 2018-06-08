package com.github.naxos84.spaceshooter.model;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.github.naxos84.spaceshooter.SpaceShooter;

public class Ship {

    private static final int MAX_HEALTH = 100;
    private static final int MIN_HEALTH = 0;

    private float hSpeed = 200;
    private float vSpeed = 200;
    private Rectangle rect;
    private int currentHealth;

    public Ship(final float x, final float y, final float height, final float width) {
        this.rect = new Rectangle(x, y, width, height);
        currentHealth = MAX_HEALTH;
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

    public boolean overlaps(final Rectangle other) {
        return rect.overlaps(other);
    }

    public Rectangle getCollisionBox() {
        return rect;
    }
}
