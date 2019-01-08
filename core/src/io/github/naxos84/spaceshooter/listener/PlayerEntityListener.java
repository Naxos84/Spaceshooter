package io.github.naxos84.spaceshooter.listener;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import io.github.naxos84.spaceshooter.model.Ship;

public class PlayerEntityListener implements EntityListener {
    @Override
    public void entityAdded(final Entity entity) {
        if (entity instanceof Ship) {
            playerAdded((Ship)entity);
        }
    }

    @Override
    public void entityRemoved(final Entity entity) {
        if (entity instanceof Ship) {
            playerRemoved((Ship)entity);
        }
    }

    public void playerAdded(final Ship ship) {

    }

    public void playerRemoved(final Ship ship) {

    }
}
