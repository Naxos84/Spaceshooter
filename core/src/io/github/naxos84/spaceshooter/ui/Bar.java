package io.github.naxos84.spaceshooter.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Bar {

    private TextureRegion barLeft;
    private TextureRegion barMid;
    private TextureRegion barRight;

    private int x, y;
    private float widthScale, heightScale;

    public Bar(final TextureRegion barLeft, TextureRegion barMid, TextureRegion barRight, int x, int y, float widthScale, int heightScale) {
        this.barLeft = barLeft;
        this.barMid = barMid;
        this.barRight = barRight;
        this.x = x;
        this.y = y;
        this.widthScale = widthScale;
        this.heightScale = heightScale;
    }

    public Bar(final TextureRegion barLeft, TextureRegion barMid, TextureRegion barRight, int x, int y) {
        this(barLeft, barMid, barRight, x, y, 1, 1);
    }

    public void render(SpriteBatch batch, final float barWidth) {
        batch.draw(barLeft, x, y, 6 * widthScale, 15 * heightScale);
        batch.draw(barMid, x + (6 * widthScale), y, barWidth, 15 * heightScale);
        batch.draw(barRight, x + (6 * widthScale) + barWidth, y, 6 * widthScale, 15 * heightScale);
    }
}
