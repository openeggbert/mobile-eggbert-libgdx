package Microsoft.Xna.Framework.Audio;

import com.badlogic.gdx.audio.Sound;

public class SoundEffect {
    private final Sound gdxSound;
    public static float MasterVolume = 1f;

    public SoundEffect(Sound gdxSound) {
        this.gdxSound = gdxSound;
    }

    public SoundEffectInstance CreateInstance() {
        return new SoundEffectInstance(gdxSound);
    }

    public void Dispose() {
        gdxSound.dispose();
    }
}
