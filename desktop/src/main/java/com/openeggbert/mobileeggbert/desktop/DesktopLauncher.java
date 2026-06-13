package com.openeggbert.mobileeggbert.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.openeggbert.mobileeggbert.Game1;

public class DesktopLauncher {
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Mobile Eggbert");
        config.setWindowedMode(640, 480);
        config.useVsync(true);
        config.setForegroundFPS(20);

        new Lwjgl3Application(new Game1(), config);
    }
}
