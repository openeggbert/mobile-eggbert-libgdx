package com.openeggbert.mobileeggbert.android;

import android.os.Bundle;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.openeggbert.mobileeggbert.EnvClasses;
import com.openeggbert.mobileeggbert.Env;
import com.openeggbert.mobileeggbert.Game1;

public class AndroidLauncher extends AndroidApplication {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Env.Init(EnvClasses.Impl.JXNA, EnvClasses.Platform.Android);

        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useImmersiveMode = true;

        initialize(new Game1(), config);
    }
}
