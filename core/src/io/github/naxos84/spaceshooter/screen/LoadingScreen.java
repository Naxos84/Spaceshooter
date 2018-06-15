package io.github.naxos84.spaceshooter.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.naxos84.spaceshooter.SpaceShooter;
import io.github.naxos84.spaceshooter.manager.ScreenManager;

public class LoadingScreen implements Screen {
    private static final int ASSETS = 1;
    private final SpaceShooter game;
    private TextArea area;
    private Stage stage;
    private int currentLoadingStage = 0;

    // timer for exiting loading screen
    private float countDown = 1f; // 5 seconds of waiting before menu screen

    public LoadingScreen(final SpaceShooter game, final boolean debug) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
        stage.clear();
        Skin skin = new Skin(Gdx.files.internal("skin/star-soldier/star-soldier-ui.json"));
        area = new TextArea(game.bundle.get("KEY_LOADING"), skin);
        stage.addActor(area);
    }

    @Override
    public void render(final float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Math.min(delta, 1 / 30f));
        stage.draw();

        if (game.getAssetManager().update()) {
            currentLoadingStage += 1;
            switch (currentLoadingStage) {
                case ASSETS:
                    area.setText("Loading Assets...");
                    game.getAssetManager().loadAssets();
                    break;
                case 2:
                    area.setText("Finished");
                    break;
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
