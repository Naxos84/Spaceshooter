package io.github.naxos84.spaceshooter.renderer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import io.github.naxos84.spaceshooter.manager.SpaceshooterAssetManager;
import io.github.naxos84.spaceshooter.model.Laser;

public class LaserRenderer {

    private Texture texture;

    public LaserRenderer(final SpaceshooterAssetManager assetManager) {
        this.texture = assetManager.getLaserTexture();
    }

    public void render(final SpriteBatch batch, final Laser laser) {
        batch.draw(texture, laser.getX(), laser.getY(), laser.getWidth(), laser.getHeight());
    }

    public void renderDebug(final ShapeRenderer renderer, final Laser laser) {
        renderer.rect(laser.getX(), laser.getY(), laser.getWidth(), laser.getHeight());
    }
}
