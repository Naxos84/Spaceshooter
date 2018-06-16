package io.github.naxos84.spaceshooter.manager;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class SpaceshooterAssetManager {

    private final AssetManager manager = new AssetManager();

    private final String shipTexturePath = "images/player/playerShip1_blue.png";
    private final String laserTexturePath = "images/lasers/laserBlue01.png";

    private final String laserSoundPath = "audio/laser5.ogg";
    private final String asteroidExplosionSoundPath = "audio/explosion_asteroid.ogg";

    private final String menuMusicPath = "audio/menu_screen_loop.ogg";
    private final String gameMusicPath = "audio/game_background_loop.ogg";

    private final String asteroidsAtlasPath = "textures/asteroids.atlas";
    private final String barsAtlasPath = "textures/bars.atlas";
    private final String enemiesAtlasPath = "textures/enemies.atlas";
    private final String healthBarLeftRegion = "barHorizontal_red_left";
    private final String healthBarMidRegion = "barHorizontal_red_mid";
    private final String healthBarRightRegion = "barHorizontal_red_right";
    private final String energyBarLeftRegion = "barHorizontal_blue_left";
    private final String energyBarMidRegion = "barHorizontal_blue_mid";
    private final String energyBarRightRegion = "barHorizontal_blue_right";

    private final String skinPath = "skin/kenney/kenney-test2.json";

    public void loadInitialAssets() {

    }

    public void loadTextures() {
        manager.load(shipTexturePath, Texture.class);
        manager.load(laserTexturePath, Texture.class);
        manager.load(asteroidsAtlasPath, TextureAtlas.class);
        manager.load(barsAtlasPath, TextureAtlas.class);
        manager.load(enemiesAtlasPath, TextureAtlas.class);
    }

    public void loadSounds() {
        manager.load(laserSoundPath, Sound.class);
        manager.load(asteroidExplosionSoundPath, Sound.class);
    }

    public void loadMusic() {
        manager.load(menuMusicPath, Music.class);
        manager.load(gameMusicPath, Music.class);
    }

    public void loadSkins() {
        manager.load(skinPath, Skin.class);
    }

    public void finishLoading() {
        manager.finishLoading();
    }

    public Texture getShipTexture() {
        return manager.get(shipTexturePath);
    }

    public Texture getLaserTexture() {
        return manager.get(laserTexturePath);
    }

    public Sound getLaserSound() {
        return manager.get(laserSoundPath);
    }

    public Sound getAsteroidExplosion() {
        return manager.get(asteroidExplosionSoundPath);
    }

    public TextureAtlas getAsteroidsAtlas() {
        return manager.get(asteroidsAtlasPath);
    }

    public TextureAtlas.AtlasRegion getAsteroid(final int index) {
        return getAsteroidsAtlas().getRegions().get(index);
    }

    public int getNumberOfAsteroids() {
        return getAsteroidsAtlas().getRegions().size;
    }

    public TextureAtlas getBarsAtlas() {
        return manager.get(barsAtlasPath);
    }

    public TextureAtlas.AtlasRegion getHealthBarLeft() {
        return getBarsAtlas().findRegion(healthBarLeftRegion);
    }

    public TextureAtlas.AtlasRegion getHealthBarMid() {
        return getBarsAtlas().findRegion(healthBarMidRegion);
    }

    public TextureAtlas.AtlasRegion getHealthBarRight() {
        return getBarsAtlas().findRegion(healthBarRightRegion);
    }

    public TextureAtlas.AtlasRegion getEnergyBarLeft() {
        return getBarsAtlas().findRegion(energyBarLeftRegion);
    }

    public TextureAtlas.AtlasRegion getEnergyBarMid() {
        return getBarsAtlas().findRegion(energyBarMidRegion);
    }

    public TextureAtlas.AtlasRegion getEnergyBarRight() {
        return getBarsAtlas().findRegion(energyBarRightRegion);
    }

    public TextureAtlas getEnemiesAtlas() {
        return manager.get(enemiesAtlasPath);
    }

    public int getNumberOfEnemies() {
        return getEnemiesAtlas().getRegions().size;
    }

    public TextureAtlas.AtlasRegion getEnemy(final int index) {
        return getEnemiesAtlas().getRegions().get(index);
    }

    public Music getMenuMusic() {
        return manager.get(menuMusicPath);
    }

    public Music getGameMusic() {
        return manager.get(gameMusicPath);
    }

    public Skin getUiSkin() {
        return manager.get(skinPath);
    }

    public boolean update() {
        return manager.update();
    }

    public void dispose() {
        manager.dispose();
    }
}
