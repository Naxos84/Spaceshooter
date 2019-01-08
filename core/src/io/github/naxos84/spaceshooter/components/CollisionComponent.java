package io.github.naxos84.spaceshooter.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

public class CollisionComponent implements Component {

    private Entity collisionEntity;

    public CollisionComponent(Entity entity) {
        collisionEntity = entity;
    }

    /**
     * handles the collision between the internal collisitionEntity and the colliding entity.
     * @param collidedEntity the entity that is colliding with the internal collision entity
     */
    public void handleCollision(final Entity collidedEntity) {
        AttributesComponent attributes = collisionEntity.getComponent(AttributesComponent.class);
        DamageComponent damage = collisionEntity.getComponent(DamageComponent.class);

        AttributesComponent collidingAttributes = collidedEntity.getComponent(AttributesComponent.class);
        DamageComponent collidingDamage = collidedEntity.getComponent(DamageComponent.class);
        if (collidingAttributes != null && damage != null) {
            collidingAttributes.reduceHealth(damage.getDamage());
        }
        if (attributes != null && collidingDamage != null) {
            attributes.reduceHealth(collidingDamage.getDamage());
        }
    }
}
