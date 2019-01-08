package io.github.naxos84.spaceshooter.manager;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class SpaceshooterAssetManager {

    private final AssetManager manager = new AssetManager();

    private static final String SHIP_TEXTURE_PATH = "images/player/playerShip1_blue.png";
    private static final String LASER_TEXTURE_PATH = "images/lasers/laserBlue01.png";

    private static final String LASER_SOUND_PATH = "audio/laser5.ogg";
    private static final String ASTEROID_EXPLOSION_SOUND_PATH = "audio/explosion_asteroid.ogg";

    private static final String MENU_MUSIC_PATH = "audio/menu_screen_loop.ogg";
    private static final String GAME_MUSIC_PATH = "audio/game_background_loop.ogg";

    private static final String ASTEROIDS_ATLAS_PATH = "textures/asteroids.atlas";
    private static final String BARS_ATLAS_PATH = "textures/bars.atlas";
    private static final String ENEMIES_ATLAS_PATH = "textures/enemies.atlas";
    private static final String HEALTH_BAR_LEFT_REGION = "barHorizontal_red_left";
    private static final String HEALTH_BAR_MID_REGION = "barHorizontal_red_mid";
    private static final String HEALTH_BAR_RIGHT_REGION = "barHorizontal_red_right";
    private static final String ENERGY_BAR_LEFT_REGION = "barHorizontal_blue_left";
    private static final String ENERGY_BAR_MID_REGION = "barHorizontal_blue_mid";
    private static final String ENERGY_BAR_RIGHT_REGION = "barHorizontal_blue_right";

    private static final String SKIN_PATH = "skin/kenney/kenney-test2.json";

    public void loadTextures() {
        manager.load(SHIP_TEXTURE_PATH, Texture.class);
        manager.load(LASER_TEXTURE_PATH, Texture.class);
        manager.load(ASTEROIDS_ATLAS_PATH, TextureAtlas.class);
        manager.load(BARS_ATLAS_PATH, TextureAtlas.class);
        manager.load(ENEMIES_ATLAS_PATH, TextureAtlas.class);
    }

    public void loadSounds() {
        manager.load(LASER_SOUND_PATH, Sound.class);
        manager.load(ASTEROID_EXPLOSION_SOUND_PATH, Sound.class);
    }

    public void loadMusic() {
        manager.load(MENU_MUSIC_PATH, Music.class);
        manager.load(GAME_MUSIC_PATH, Music.class);
    }

    public void loadSkins() {
        manager.load(SKIN_PATH, Skin.class);
    }

    public void finishLoading() {
        manager.finishLoading();
    }

    public Texture getShipTexture() {
        return manager.get(SHIP_TEXTURE_PATH);
    }

    public Texture getLaserTexture() {
        return manager.get(LASER_TEXTURE_PATH);
    }

    public Sound getLaserSound() {
        return manager.get(LASER_SOUND_PATH);
    }

    public Sound getAsteroidExplosion() {
        return manager.get(ASTEROID_EXPLOSION_SOUND_PATH);
    }

    public TextureAtlas getAsteroidsAtlas() {
        return manager.get(ASTEROIDS_ATLAS_PATH);
    }

    public TextureAtlas.AtlasRegion getAsteroid(final int index) {
        return getAsteroidsAtlas().getRegions().get(index);
    }

    public int getNumberOfAsteroids() {
        return getAsteroidsAtlas().getRegions().size;
    }

    public TextureAtlas getBarsAtlas() {
        return manager.get(BARS_ATLAS_PATH);
    }

    public TextureAtlas.AtlasRegion getHealthBarLeft() {
        return getBarsAtlas().findRegion(HEALTH_BAR_LEFT_REGION);
    }

    public TextureAtlas.AtlasRegion getHealthBarMid() {
        return getBarsAtlas().findRegion(HEALTH_BAR_MID_REGION);
    }

    public TextureAtlas.AtlasRegion getHealthBarRight() {
        return getBarsAtlas().findRegion(HEALTH_BAR_RIGHT_REGION);
    }

    public TextureAtlas.AtlasRegion getEnergyBarLeft() {
        return getBarsAtlas().findRegion(ENERGY_BAR_LEFT_REGION);
    }

    public TextureAtlas.AtlasRegion getEnergyBarMid() {
        return getBarsAtlas().findRegion(ENERGY_BAR_MID_REGION);
    }

    public TextureAtlas.AtlasRegion getEnergyBarRight() {
        return getBarsAtlas().findRegion(ENERGY_BAR_RIGHT_REGION);
    }

    public TextureAtlas getEnemiesAtlas() {
        return manager.get(ENEMIES_ATLAS_PATH);
    }

    public int getNumberOfEnemies() {
        return getEnemiesAtlas().getRegions().size;
    }

    public TextureAtlas.AtlasRegion getEnemy(final int index) {
        return getEnemiesAtlas().getRegions().get(index);
    }

    public Music getMenuMusic() {
        return manager.get(MENU_MUSIC_PATH);
    }

    public Music getGameMusic() {
        return manager.get(GAME_MUSIC_PATH);
    }

    public Skin getUiSkin() {
        return manager.get(SKIN_PATH);
    }

    public boolean update() {
        return manager.update();
    }

    public void dispose() {
        manager.dispose();
    }
}
