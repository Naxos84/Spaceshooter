package io.github.naxos84.spaceshooter.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import io.github.naxos84.spaceshooter.Families;
import io.github.naxos84.spaceshooter.components.*;
import io.github.naxos84.spaceshooter.manager.SpaceshooterAssetManager;
import io.github.naxos84.spaceshooter.ui.Bar;

public class RenderSystem extends IteratingSystem {

    private final boolean debug;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private Array<Entity> renderQueue;
    private OrthographicCamera camera;
    private SpaceshooterAssetManager assetManager;

    private ComponentMapper<TextureComponent> textureMapper;
    private ComponentMapper<PositionComponent> positionMapper;
    private ComponentMapper<AttributesComponent> attributesMapper;

    public RenderSystem(final SpriteBatch batch, final ShapeRenderer shapeRenderer, final OrthographicCamera camera, SpaceshooterAssetManager assetManager, final boolean debug) {
        super(Family.all(PositionComponent.class, TextureComponent.class).get());

        this.debug = debug;

        textureMapper = ComponentMapper.getFor(TextureComponent.class);
        positionMapper = ComponentMapper.getFor(PositionComponent.class);
        attributesMapper = ComponentMapper.getFor(AttributesComponent.class);

        renderQueue = new Array<>();
        this.batch = batch;
        this.shapeRenderer = shapeRenderer;
        this.camera = camera;
        this.assetManager = assetManager;
    }

    @Override
    public void update(final float deltaTime) {
        super.update(deltaTime);

        camera.update();
        render();

        if (debug) {
            renderDebug();
        }
        renderQueue.clear();
    }

    private void render() {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for (Array.ArrayIterator<Entity> entityIterator = new Array.ArrayIterator<>(renderQueue); entityIterator.hasNext(); ) {
            Entity entity = entityIterator.next();
            TextureComponent texture = textureMapper.get(entity);
            PositionComponent position = positionMapper.get(entity);
            SizeComponent size = entity.getComponent(SizeComponent.class);
            renderEntity(texture, position, size);

            AttributesComponent attributes = attributesMapper.get(entity);
            if (Families.getHazards().matches(entity) && attributes.getHealth() < attributes.getMaxHealth()) {
                renderHealthBar(position, size, attributes);
            }
        }
        batch.end();
    }

    private void renderEntity(TextureComponent texture, PositionComponent position, SizeComponent size) {
        if (texture.region == null || position.isHidden) {
            Gdx.app.log(this.getClass().getName(), "Invalid entity component.");
            return;
        }
        batch.draw(texture.region, position.x, position.y, 0, 0, size.getWidth(), size.getHeight(), 1, 1, 0f);
    }

    private void renderHealthBar(PositionComponent position, SizeComponent size, AttributesComponent attributes) {
        if (position.isHidden) {
            Gdx.app.log(this.getClass().getName(), "Hidden position");
            return;
        }
            float healthWidth = attributes.getHealth() / attributes.getMaxHealth() * size.getWidth();
            TextureAtlas.AtlasRegion healthBarTexture = assetManager.getHealthBarMid();
            batch.draw(healthBarTexture, position.x, position.y + size.getHeight() + 2, healthWidth, 5);
    }

    private void renderDebug() {
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.begin();
        for (Array.ArrayIterator<Entity> entityIterator = new Array.ArrayIterator<>(renderQueue); entityIterator.hasNext(); ) {
            Entity entity = entityIterator.next();
            TextureComponent texture = textureMapper.get(entity);
            PositionComponent position = positionMapper.get(entity);
            SizeComponent size = entity.getComponent(SizeComponent.class);

            if (texture.region == null || position.isHidden) {
                Gdx.app.log(this.getClass().getName(), "Invalid entity component for debug rendering.");
                continue;
            }

            shapeRenderer.rect(position.x, position.y, size.getWidth(), size.getHeight());

        }
        shapeRenderer.end();
    }

    @Override
    protected void processEntity(final Entity entity, final float deltaTime) {
        renderQueue.add(entity);
    }
}
