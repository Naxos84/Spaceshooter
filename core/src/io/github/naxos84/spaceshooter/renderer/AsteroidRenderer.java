package io.github.naxos84.spaceshooter.renderer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import io.github.naxos84.spaceshooter.model.Asteroid;

public class AsteroidRenderer {

    private Array<TextureAtlas.AtlasRegion> asteroidsRegions;
    private TextureRegion healthBarMid;

    public AsteroidRenderer(final Array<TextureAtlas.AtlasRegion> asteroidsRegions, final TextureAtlas barsAtlas) {
        this.asteroidsRegions = asteroidsRegions;
        healthBarMid = barsAtlas.findRegion("barHorizontal_red_mid");
    }

    public void render(final SpriteBatch batch, final Asteroid asteroid) {
        batch.draw(asteroidsRegions.get(asteroid.getId()), asteroid.getX(), asteroid.getY(), asteroid.getWidth(), asteroid.getHeight());
        float healthBarMaxWidth = asteroid.getWidth();
        if (!asteroid.isFullLife()) {
            batch.draw(healthBarMid, asteroid.getX(), asteroid.getY() + asteroid.getHeight(), asteroid.getCurrentHealth() / asteroid.getMaxHealth() * healthBarMaxWidth, 5);
        }

    }

    public void renderDebug(final ShapeRenderer renderer, final Asteroid asteroid) {
        renderer.rect(asteroid.getX(), asteroid.getY(), asteroid.getWidth(), asteroid.getHeight());
    }
}
