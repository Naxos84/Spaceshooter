package io.github.naxos84.spaceshooter.model;

import io.github.naxos84.spaceshooter.components.AttributesComponent;

public class Ship extends GameObject {

    public static final int MAX_HEALTH = 100;

    public static final int MAX_ENERGY = 300;


    public Ship(final float x, final float y, final int width, final int height) {
        super(x, y, width, height);
        add(new AttributesComponent(Ship.MAX_HEALTH, Ship.MAX_ENERGY, Ship.MAX_HEALTH, Ship.MAX_ENERGY));
    }
}
