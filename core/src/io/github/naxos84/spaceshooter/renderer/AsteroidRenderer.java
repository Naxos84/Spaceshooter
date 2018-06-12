package io.github.naxos84.spaceshooter.renderer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import io.github.naxos84.spaceshooter.model.Asteroid;

public class AsteroidRenderer {

    private Texture texture;

    public AsteroidRenderer(final Texture texture) {
        this.texture = texture;
    }

    public void render(final SpriteBatch batch, final Asteroid asteroid) {
        batch.draw(texture, asteroid.getX(), asteroid.getY(), asteroid.getWidth(), asteroid.getHeight());
    }

    public void renderDebug(final ShapeRenderer renderer, final Asteroid asteroid) {
        renderer.rect(asteroid.getX(),asteroid.getY() ,asteroid.getWidth() ,asteroid.getHeight() );
    }
}
