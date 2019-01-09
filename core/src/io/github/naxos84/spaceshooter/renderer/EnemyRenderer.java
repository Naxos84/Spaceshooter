package io.github.naxos84.spaceshooter.renderer;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import io.github.naxos84.spaceshooter.manager.SpaceshooterAssetManager;
import io.github.naxos84.spaceshooter.model.Enemy;

public class EnemyRenderer {

    private final Array<TextureAtlas.AtlasRegion> enemiesRegions;
    private final TextureRegion healthBarMid;

    public EnemyRenderer(final SpaceshooterAssetManager assetManager) {
        enemiesRegions = assetManager.getEnemiesAtlas().getRegions();
        healthBarMid = assetManager.getHealthBarMid();
    }

    public void render(final SpriteBatch batch, final Enemy enemy) {
        batch.draw(enemiesRegions.get(enemy.getId()), enemy.getX(), enemy.getY(), enemy.getWidth(), enemy.getHeight());
        float healthBarMaxWidth = enemy.getWidth();
        if (!enemy.isFullLife()) {
            batch.draw(healthBarMid, enemy.getX(), enemy.getY() + enemy.getHeight(), enemy.getCurrentHealth() / enemy.getMaxHealth() * healthBarMaxWidth, 5);
        }

    }

    public void renderDebug(final ShapeRenderer renderer, final Enemy enemy) {
        renderer.rect(enemy.getX(), enemy.getY(), enemy.getWidth(), enemy.getHeight());
    }
}
