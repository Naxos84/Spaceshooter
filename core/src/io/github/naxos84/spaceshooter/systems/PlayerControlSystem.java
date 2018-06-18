package io.github.naxos84.spaceshooter.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.github.naxos84.spaceshooter.components.MoverComponent;
import io.github.naxos84.spaceshooter.components.PlayerComponent;
import io.github.naxos84.spaceshooter.components.PositionComponent;
import io.github.naxos84.spaceshooter.components.TextureComponent;
import io.github.naxos84.spaceshooter.controller.KeyboardController;

import javax.xml.soap.Text;

public class PlayerControlSystem extends IteratingSystem {

    ComponentMapper<PositionComponent> positionMapper;
    KeyboardController keyboardController;

    public PlayerControlSystem(final KeyboardController keyboardController) {
        super(Family.all(PositionComponent.class).get());
        this.keyboardController = keyboardController;

        positionMapper = ComponentMapper.getFor(PositionComponent.class);
    }

    @Override
    protected void processEntity(final Entity entity, final float deltaTime) {
        PositionComponent position = entity.getComponent(PositionComponent.class);
//        PositionComponent position = positionMapper.get(entity);
        if (position != null) {
            if (keyboardController.isLeftPressed()) {
                position.x -= 200 * deltaTime;
            }
            if (keyboardController.isRightPressed()) {
                position.x += 200 * deltaTime;
            }
            if (keyboardController.isUpPressed()) {
                position.y += 200 * deltaTime;
            }
            if (keyboardController.isDownPressed()) {
                position.y -= 200 * deltaTime;
            }
            if (keyboardController.isSpacePressed()) {
                Entity laser = new Entity();
                PositionComponent positionComponent = new PositionComponent();
                positionComponent.x = 100;
                positionComponent.y = 100;
                laser.add(positionComponent);
                TextureComponent textureComponent = new TextureComponent();
                textureComponent.region = new TextureRegion(new Texture(Gdx.files.internal("images/lasers/laserBlue01.png")));
                laser.add(textureComponent);
                MoverComponent mover = new MoverComponent();
                mover.speed = 600;
                laser.add(mover);
                getEngine().addEntity(laser);
            }
        } else {
            Gdx.app.log("ERROR", "No position available.");
        }
    }

    @Override
    public void update(final float deltaTime) {
        super.update(deltaTime);
    }
}
