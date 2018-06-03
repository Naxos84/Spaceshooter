package com.github.naxos84.spaceshooter;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;

public class Asteroid extends Rectangle implements Disposable {

    private int rotation;
    private Texture texture;
    private TextureRegion textureRegion;

    public Asteroid(final Texture texture, final int rotation) {
        this.texture = texture;
        this.textureRegion = new TextureRegion(this.texture);
        this.rotation = rotation;
    }

    public TextureRegion getTextureRegion() {
        return textureRegion;
    }

    public int getRotation() {
        return rotation;
    }

    @Override
    public void dispose() {
        this.texture.dispose();
    }
}
