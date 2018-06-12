package io.github.naxos84.spaceshooter.renderer;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import io.github.naxos84.spaceshooter.model.Asteroid;

public class AsteroidRenderer {

    private Array<TextureAtlas.AtlasRegion> regions;

    public AsteroidRenderer(final Array<TextureAtlas.AtlasRegion> regions) {
        this.regions = regions;
    }

    public void render(final SpriteBatch batch, final Asteroid asteroid) {
        batch.draw(regions.get(asteroid.getId()), asteroid.getX(), asteroid.getY(), asteroid.getWidth(), asteroid.getHeight());
    }

    public void renderDebug(final ShapeRenderer renderer, final Asteroid asteroid) {
        renderer.rect(asteroid.getX(), asteroid.getY(), asteroid.getWidth(), asteroid.getHeight());
    }
}
