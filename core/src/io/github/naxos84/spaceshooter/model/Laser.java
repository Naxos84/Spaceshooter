package io.github.naxos84.spaceshooter.model;

import com.badlogic.gdx.math.Rectangle;
import io.github.naxos84.spaceshooter.SpaceShooter;

public class Laser {

    private int damage = 1;
    private float speed = 600;
    private Rectangle rect;

    public Laser(final float x, final float y, final float width, final float height) {
        this.rect = new Rectangle(x, y, width, height);
    }

    public void updatePosition(final float deltaTime) {
        rect.x += speed * deltaTime;
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

    public boolean overlaps(final Rectangle other) {
        return rect.overlaps(other);
    }

    public Rectangle getCollisionBox() {
        return rect;
    }

    public int getDamage() {
        return damage;
    }

    public boolean isDead() {
        return rect.x - rect.width > SpaceShooter.SCREEN_WIDTH;
    }
}
