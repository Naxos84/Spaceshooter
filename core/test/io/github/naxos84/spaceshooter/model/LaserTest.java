package com.github.naxos84.spaceshooter.model;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LaserTest {

    @Test
    public void getX() {
        Laser l = new Laser(1, 2, 3, 4);
        assertThat(l.getX()).isEqualTo(1);
    }

    @Test
    public void getY() {
        Laser l = new Laser(1, 2, 3, 4);
        assertThat(l.getY()).isEqualTo(2);
    }

    @Test
    public void getWidth() {
        Laser l = new Laser(1, 2, 3, 4);
        assertThat(l.getWidth()).isEqualTo(3);
    }

    @Test
    public void getHeight() {
        Laser l = new Laser(1, 2, 3, 4);
        assertThat(l.getHeight()).isEqualTo(4);
    }
}