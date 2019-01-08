package io.github.naxos84.spaceshooter.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.naxos84.spaceshooter.SpaceShooter;
import io.github.naxos84.spaceshooter.manager.ScreenManager;

public class LoadingScreen implements Screen {
    private static final int FONTS = 1;
    private static final int TEXTURES = 2;
    private static final int SKINS = 3;
    private static final int MUSICS = 4;
    private static final int SOUNDS = 5;
    private static final int FINISHED = 6;
    private final SpaceShooter game;
    private int currentLoadingStage = 0;
    private GlyphLayout layout;
    private String currentText = "";

    private OrthographicCamera camera;

    // timer for exiting loading screen
    private float countDown = 1f; // 5 seconds of waiting before menu screen

    public LoadingScreen(final SpaceShooter game, final boolean debug) {
        this.game = game;
        layout = new GlyphLayout();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(final float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (game.getAssetManager().update()) {
            currentLoadingStage += 1;
            switch (currentLoadingStage) {
                case TEXTURES:
                    currentText = game.bundle.get("KEY_LOAD_TEXTURE");
                    game.getAssetManager().loadTextures();
                    break;
                case SKINS:
                    currentText = game.bundle.get("KEY_LOAD_SKIN");
                    game.getAssetManager().loadSkins();
                    break;
                case MUSICS:
                    currentText = game.bundle.get("KEY_LOAD_MUSIC");
                    game.getAssetManager().loadMusic();
                    break;
                case SOUNDS:
                    currentText = game.bundle.get("KEY_LOAD_SOUND");
                    game.getAssetManager().loadSounds();
                    break;
                case FINISHED:
                    currentText = game.bundle.get("KEY_FINISHED_LOADING");
                    break;
                    default:
                        currentText = game.bundle.get("KEY_NOT_FOUND");
            }
            if (currentLoadingStage > 5) {
                countDown -= delta;  // timer to stay on loading screen for short preiod once done loading
                currentLoadingStage = 5;  // cap loading stage to 5 as will use later to display progress bar anbd more than 5 would go off the screen
                if (countDown < 0) { // countdown is complete
                    game.getAudioManager().load();
                    game.changeScreen(ScreenManager.MENU);
                }
            }
        }
        layout.setText(game.font, currentText);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.font.draw(game.batch, currentText, SpaceShooter.SCREEN_WIDTH / 2 - layout.width / 2, layout.height + 10);
        game.batch.end();
    }

    @Override
    public void resize(final int width, final int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
