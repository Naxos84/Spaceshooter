package io.github.naxos84.spaceshooter.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import io.github.naxos84.spaceshooter.components.PositionComponent;
import io.github.naxos84.spaceshooter.components.SizeComponent;
import io.github.naxos84.spaceshooter.components.TextureComponent;

public class RenderSystem extends IteratingSystem {

    private final boolean debug;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private Array<Entity> renderQueue;
    private OrthographicCamera camera;

    private ComponentMapper<TextureComponent> textureMapper;
    private ComponentMapper<PositionComponent> positionMapper;

    public RenderSystem(final SpriteBatch batch, final ShapeRenderer shapeRenderer, final boolean debug) {
        super(Family.all(PositionComponent.class, TextureComponent.class).get());

        this.debug = debug;

        textureMapper = ComponentMapper.getFor(TextureComponent.class);
        positionMapper = ComponentMapper.getFor(PositionComponent.class);

        renderQueue = new Array<>();
        this.batch = batch;
        this.shapeRenderer = shapeRenderer;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);
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

            if (texture.region == null || position.isHidden) {
                continue;
            }

            float width = texture.region.getRegionWidth();
            float height = texture.region.getRegionHeight();

            float originX = width / 2f;
            float originY = height / 2f;

            batch.draw(texture.region, position.x, position.y, originX, originY, size == null ? width : size.getWidth(), size == null ? height : size.getHeight(), 1, 1, 0f);

        }
        batch.end();
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
                continue;
            }

            float width = texture.region.getRegionWidth();
            float height = texture.region.getRegionHeight();

            shapeRenderer.rect(position.x, position.y, size == null ? width : size.getWidth(), size == null ? height : size.getHeight());

        }
        shapeRenderer.end();
    }

    @Override
    protected void processEntity(final Entity entity, final float deltaTime) {
        renderQueue.add(entity);
    }
}
