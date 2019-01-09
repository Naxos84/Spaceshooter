package io.github.naxos84.spaceshooter.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import io.github.naxos84.spaceshooter.SpaceShooter;

public class DesktopLauncher {
    public static void main(String[] arg) {
        boolean debugMode = false;
        if (arg.length > 0 && arg[0].equals("debug")) {
            debugMode = true;
        }

        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Spaceshooter - Endless Conflict";
        config.height = 600;
        config.width = 800;
        config.addIcon("icons/gameicon_128x128.png", Files.FileType.Internal);
        config.addIcon("icons/gameicon_32x32.png", Files.FileType.Internal);
        config.addIcon("icons/gameicon_16x16.png", Files.FileType.Internal);
        config.forceExit = false;
        config.preferencesDirectory = "AppData/Local/Spaceshooter - Endless Conflict/preferences";

        new LwjglApplication(new SpaceShooter(debugMode), config);
    }
}
