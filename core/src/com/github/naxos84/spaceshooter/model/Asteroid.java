package com.github.naxos84.spaceshooter.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;

public class Asteroid extends Rectangle {

    private int rotation;
    private TextureRegion textureRegion;

    public Asteroid(final Texture texture, final int rotation) {
        this.textureRegion = new TextureRegion(texture);
        this.rotation = rotation;
    }

    public TextureRegion getTextureRegion() {
        return textureRegion;
    }

    public int getRotation() {
        return rotation;
    }
}
