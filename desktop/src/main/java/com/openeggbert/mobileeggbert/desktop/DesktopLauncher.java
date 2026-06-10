package com.openeggbert.mobileeggbert.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.openeggbert.mobileeggbert.EnvClasses;
import com.openeggbert.mobileeggbert.Env;
import com.openeggbert.mobileeggbert.Game1;

public class DesktopLauncher {
    public static void main(String[] args) {
        Env.Init(EnvClasses.Impl.JXNA, EnvClasses.Platform.Desktop);

        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Mobile Eggbert");
        config.setWindowedMode(640, 480);
        config.useVsync(true);
        config.setForegroundFPS(60);

        new Lwjgl3Application(new Game1(), config);
    }
}
