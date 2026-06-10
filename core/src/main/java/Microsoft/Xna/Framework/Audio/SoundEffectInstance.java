package Microsoft.Xna.Framework.Audio;

import com.badlogic.gdx.audio.Sound;

public class SoundEffectInstance {
    private final Sound gdxSound;
    private long soundId = -1;
    public float Volume = 1f;
    public float Pan = 0f;
    public float Pitch = 0f;
    public boolean IsLooped = false;
    private SoundState state = SoundState.Stopped;

    public SoundEffectInstance(Sound gdxSound) {
        this.gdxSound = gdxSound;
    }

    public SoundState State() { return state; }

    public void Play() {
        if (IsLooped) {
            soundId = gdxSound.loop(Volume, 1f + Pitch * 0.5f, Pan);
        } else {
            soundId = gdxSound.play(Volume, 1f + Pitch * 0.5f, Pan);
        }
        state = SoundState.Playing;
    }

    public void Stop() {
        if (soundId >= 0) {
            gdxSound.stop(soundId);
        }
        state = SoundState.Stopped;
        soundId = -1;
    }

    public void Dispose() {
        Stop();
        gdxSound.dispose();
    }
}
