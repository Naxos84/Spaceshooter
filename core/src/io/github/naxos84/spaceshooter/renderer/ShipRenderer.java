package io.github.naxos84.spaceshooter.renderer;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import io.github.naxos84.spaceshooter.manager.SpaceshooterAssetManager;
import io.github.naxos84.spaceshooter.model.Ship;

public class ShipRenderer {

    private Rectangle rect;
    private Texture texture;

    public ShipRenderer(final SpaceshooterAssetManager assetManager) {
        rect = new Rectangle();
        this.texture = assetManager.getShipTexture();
    }

    public void render(final SpriteBatch batch, final Ship ship) {
        if (ship.isAlive()) {
            batch.draw(texture, ship.getX(), ship.getY(), ship.getWidth(), ship.getHeight());
        }
    }

    public void renderDebug(final ShapeRenderer renderer, final Ship ship) {
        renderer.rect(ship.getX(), ship.getY(), ship.getWidth(), ship.getHeight());
    }
}
