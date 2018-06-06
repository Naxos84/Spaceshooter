package com.github.naxos84.spaceshooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class GamePreferences {

    private static final String PREF_MUSIC_VOLUME = "audio.music.volume";
    private static final String PREF_MUSIC_ENABLED = "audio.music.enabled";
    private static final String PREF_SOUND_VOLUME = "audio.sound.volume";
    private static final String PREF_SOUND_ENABLED = "audio.sound.enabled";

    private static final String NAME = "spaceshooter-settings";

    protected Preferences getPreferences() {
        return Gdx.app.getPreferences(NAME);
    }

    public float getMusicVolume() {
        return getPreferences().getFloat(PREF_MUSIC_VOLUME, .8f);
    }

    public void setMusicVolume(final float volume) {
        getPreferences().putFloat(PREF_MUSIC_VOLUME, volume).flush();
    }

    public boolean isMusicEnabled() {
        return getPreferences().getBoolean(PREF_MUSIC_ENABLED, true);
    }

    public void setMusicEnabled(final boolean enabled) {
        getPreferences().putBoolean(PREF_MUSIC_ENABLED, enabled).flush();
    }

    public float getSoundVolume() {
        return getPreferences().getFloat(PREF_SOUND_VOLUME, .8f);
    }

    public void setSoundVolume(final float volume) {
        getPreferences().putFloat(PREF_SOUND_VOLUME, volume).flush();
    }

    public boolean isSoundEnabled() {
        return getPreferences().getBoolean(PREF_SOUND_ENABLED, true);
    }

    public void setSoundEnabled(final boolean enabled) {
        getPreferences().putBoolean(PREF_SOUND_ENABLED, enabled).flush();
    }
}
