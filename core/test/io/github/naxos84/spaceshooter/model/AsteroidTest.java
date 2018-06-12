package io.github.naxos84.spaceshooter.model;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class AsteroidTest {

    @Test
    public void setHealth() {
        Asteroid asteroid = new Asteroid(1, 1, 1, 1, 1, 0);
        asteroid.setHealth(50);
        assertThat(asteroid.getCurrentHealth()).isEqualTo(50);
    }

    @Test
    public void setHealthLargerThanMax() {
        Asteroid asteroid = new Asteroid(1, 1, 1, 1, 1, 0);
        asteroid.setHealth(Asteroid.MAX_HEALTH + 1);
        assertThat(asteroid.getCurrentHealth()).isEqualTo(Asteroid.MAX_HEALTH);
    }

    @Test
    public void setHealthLessThanMin() {
        Asteroid asteroid = new Asteroid(1, 1, 1, 1, 1, 0);
        asteroid.setHealth(Asteroid.MIN_HEALTH - 1);
        assertThat(asteroid.getCurrentHealth()).isEqualTo(Asteroid.MIN_HEALTH);
    }

    @Test
    public void reduceHealth() {
        Asteroid asteroid = new Asteroid(1, 1, 1, 1, 1, 0);
        asteroid.setHealth(50);
        asteroid.reduceHealth(3);
        assertThat(asteroid.getCurrentHealth()).isEqualTo(47);
    }
}