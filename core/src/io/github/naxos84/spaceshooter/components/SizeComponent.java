package io.github.naxos84.spaceshooter.components;

import com.badlogic.ashley.core.Component;

public class SizeComponent implements Component {

    private int width;
    private int height;

    public SizeComponent(final int width, final int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
