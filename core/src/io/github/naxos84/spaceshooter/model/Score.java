package io.github.naxos84.spaceshooter.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.naxos84.spaceshooter.SpaceShooter;

public class Score {

    BitmapFont font = new BitmapFont();

    private int value;

    public Score() {
        font = new BitmapFont(Gdx.files.internal("skin/kenney/kenney-future2.fnt"));
    }

    public void add(final int score) {
        this.value += score;
    }

    public int get() {
        return value;
    }

    public void render(SpriteBatch batch) {
        font.draw(batch, "Score: " + String.valueOf(value), 10, SpaceShooter.HEIGHT - 10);
    }
}
