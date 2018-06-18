package io.github.naxos84.spaceshooter.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import io.github.naxos84.spaceshooter.components.PositionComponent;
import io.github.naxos84.spaceshooter.components.TextureComponent;

import java.util.Comparator;

public class RenderSystem extends IteratingSystem {

    private SpriteBatch batch;
    private Array<Entity> renderQueue;
    private Comparator<Entity> comparator;
    private OrthographicCamera camera;

    private ComponentMapper<TextureComponent> textureMapper;
    private ComponentMapper<PositionComponent> positionMapper;

    public RenderSystem(final SpriteBatch batch) {
        super(Family.all(PositionComponent.class, TextureComponent.class).get());

        textureMapper = ComponentMapper.getFor(TextureComponent.class);
        positionMapper = ComponentMapper.getFor(PositionComponent.class);

        renderQueue = new Array<>();
        this.batch = batch;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);
    }

    @Override
    public void update(final float deltaTime) {
        super.update(deltaTime);

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for (Entity entity : renderQueue) {
            TextureComponent texture = textureMapper.get(entity);
            PositionComponent position = positionMapper.get(entity);

            if (texture.region == null || position.isHidden) {
                continue;
            }

            float width = texture.region.getRegionWidth();
            float height = texture.region.getRegionHeight();

            float originX = width / 2f;
            float originY = height / 2f;

            batch.draw(texture.region, position.x, position.y, originX, originY, width, height,1,1, 0f);

        }
        batch.end();
        renderQueue.clear();
    }

    @Override
    protected void processEntity(final Entity entity, final float deltaTime) {
        renderQueue.add(entity);
    }
}
