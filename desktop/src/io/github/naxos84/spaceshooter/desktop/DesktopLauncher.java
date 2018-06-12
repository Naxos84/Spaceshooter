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
        config.height = 600;
        config.width = 800;
        //config.preferencesDirectory = "spaceshooter/preferences";
        //config.preferencesFileType = Files.FileType.External;

        new LwjglApplication(new SpaceShooter(debugMode), config);
    }
}
