package io.github.naxos84.spaceshooter.overlay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;

public class GameOver {

    private final BitmapFont font;
    private final GlyphLayout layout;
    private boolean show;

    public GameOver() {
        font = new BitmapFont(Gdx.files.internal("skin/kenney/kenney-future2.fnt"));
        layout = new GlyphLayout();
    }

    public void show() {
        show = true;
    }

    public void hide() {
        show = false;
    }

    public void render(final SpriteBatch batch, final I18NBundle bundle, final float delta) {
        if (show) {

            layout.setText(font, bundle.get("GAME_OVER"), Color.WHITE, 0, Align.center, false);
            font.draw(batch, layout, 800 / 2, 600 / 2 + layout.height / 2);
        }
    }

    public boolean isVisible() {
        return show;
    }
}
