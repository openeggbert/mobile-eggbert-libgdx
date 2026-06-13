package com.openeggbert.mobileeggbert;

import Microsoft.Xna.Framework.Audio.SoundEffect;
import Microsoft.Xna.Framework.Audio.SoundEffectInstance;
import Microsoft.Xna.Framework.Audio.SoundState;
import java.util.ArrayList;
import java.util.List;

public class Sound {
    private static class Play {
        private final SoundEffectInstance sei;
        private final int channel;

        public int Channel() { return channel; }
        public boolean IsFree() { return sei.State() == SoundState.Stopped; }

        public Play(SoundEffect se, int channel, double volume, double balance, Double pitch, boolean isLooped) {
            this.channel = channel;
            int idx = channel * 2;
            if (idx >= 0 && idx < tableVolumePitch.length) {
                volume *= tableVolumePitch[idx];
                pitch = tableVolumePitch[idx + 1];
            }
            sei = se.CreateInstance();
            sei.Volume = (float) volume;
            sei.Pan = (float) balance;
            sei.Pitch = (float)(pitch != null ? pitch : 0.0);
            sei.IsLooped = isLooped;
            sei.Play();
        }

        public void Stop() { sei.Stop(); }
    }

    private static final double[] tableVolumePitch = new double[]{
        1.0, 0.0, 0.5, 1.0, 0.5, 1.0, 1.0, 0.2, 1.0, 0.2,
        1.0, 0.1, 1.0, 0.3, 1.0, 0.2, 1.0, 0.3, 1.0, 0.5,
        1.0, 0.2, 1.0, 0.2, 1.0, 0.1, 1.0, 0.2, 1.0, 0.2,
        1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.2,
        1.0, 0.2, 1.0, 0.2, 0.7, 0.2, 1.0, 0.1, 1.0, 0.1,
        1.0, 0.2, 1.0, 0.2, 1.0, 0.4, 1.0, 0.0, 1.0, 0.0,
        1.0, 0.0, 1.0, 0.0, 1.0, 0.2, 1.0, 0.2, 0.7, 0.4,
        1.0, 0.2, 1.0, 0.4, 1.0, 0.2, 0.5, 1.0, 0.5, 1.0,
        1.0, 0.4, 1.0, 0.2, 1.0, 0.2, 1.0, 0.2, 1.0, 0.2,
        1.0, 0.2, 1.0, 0.2, 1.0, 0.2, 0.6, 0.4, 0.8, 0.1,
        0.6, 0.5, 1.0, 0.2, 1.0, 0.2, 1.0, 0.2, 1.0, 0.2,
        1.0, 0.2, 1.0, 0.2, 1.0, 0.2, 1.0, 0.2, 1.0, 0.2,
        1.0, 0.2, 1.0, 0.2, 1.0, 0.2, 1.0, 0.2, 1.0, 0.0,
        1.0, 0.2, 1.0, 0.2, 1.0, 0.2, 1.0, 0.2, 1.0, 0.0,
        1.0, 0.2, 1.0, 0.2, 1.0, 0.0, 1.0, 0.0, 1.0, 0.2,
        1.0, 0.2, 1.0, 0.2, 1.0, 0.2, 1.0, 0.2, 1.0, 0.2,
        1.0, 0.2, 1.0, 0.2, 1.0, 0.2, 1.0, 0.2, 0.6, 0.4,
        1.0, 0.2, 1.0, 0.2, 1.0, 0.2, 1.0, 0.2, 1.0, 0.2,
        1.0, 0.2, 1.0, 0.2, 1.0, 0.2, 1.0, 0.2, 1.0, 0.2,
        1.0, 0.2, 1.0, 0.2, 1.0, 0.2, 1.0, 0.2, 1.0, 0.2
    };

    public static final int MAXVOLUME = 20;

    private final Game1 game1;
    private final GameData gameData;
    private final List<SoundEffect> soundEffects;
    private final List<Play> plays;
    private double volume;

    public Sound(Game1 game1, GameData gameData) {
        this.game1 = game1;
        this.gameData = gameData;
        soundEffects = new ArrayList<>();
        plays = new ArrayList<>();
        volume = 1.0;
    }

    public void LoadContent() {
        if (Def.HasSound) {
            for (int i = 0; i <= 92; i++) {
                String assetName = "sounds/sound" + Misc.ZeroPad(i, 3);
                SoundEffect se = game1.Content.Load(SoundEffect.class, assetName);
                soundEffects.add(se);
            }
        }
    }

    public boolean Create() { return true; }
    public void SetState(boolean bState) {}
    public void SetCDAudio(boolean bAudio) {}
    public boolean Enable() { return true; }

    public void SetAudioVolume(int vol) { this.volume = (double) vol / MAXVOLUME; }
    public int AudioVolume() { return (int)(volume * MAXVOLUME); }
    public void SetMidiVolume(int vol) {}
    public int MidiVolume() { return 0; }

    public void StopAll() {
        for (Play p : plays) p.Stop();
        plays.clear();
    }

    public boolean PlayImage(int channel, TinyPoint pos) {
        return PlayImage(channel, pos, -1, false);
    }

    public boolean PlayImage(int channel, TinyPoint pos, int rank, boolean bLoop) {
        if (!gameData.Sounds()) return true;
        if (channel >= 0 && channel < soundEffects.size()) {
            boolean channelBusy = plays.stream()
                .anyMatch(p -> p.Channel() == channel && !p.IsFree());
            if (channel != 10 && channelBusy) return true;
            if (plays.size() >= 10) {
                plays.removeIf(Play::IsFree);
            }
            Play p = new Play(soundEffects.get(channel), channel,
                GetVolume(pos), GetBalance(pos), null, bLoop);
            plays.add(p);
        }
        return true;
    }

    public boolean PosImage(int channel, TinyPoint pos) { return true; }

    public boolean Stop(int channel) {
        plays.removeIf(p -> {
            if (p.Channel() == channel) { p.Stop(); return true; }
            return false;
        });
        return true;
    }

    private double GetVolume(TinyPoint pos) {
        double val = 1.0;
        if (pos.X < 0)   val = 1.0 + (double)(pos.X / 640) * 2.0;
        if (pos.X > 640) val = 1.0 - (double)((pos.X - 640) / 640) * 2.0;
        val = Math.max(0.0, Math.min(1.0, val));

        double val2 = 1.0;
        if (pos.Y < 0)   val2 = 1.0 + (double)(pos.Y / 480) * 3.0;
        if (pos.Y > 480) val2 = 1.0 - (double)((pos.Y - 480) / 480) * 3.0;
        val2 = Math.max(0.0, Math.min(1.0, val2));

        return Math.min(val, val2) * volume;
    }

    private double GetBalance(TinyPoint pos) {
        double val = (double) pos.X * 2.0 / 640.0 - 1.0;
        return Math.max(-1.0, Math.min(1.0, val));
    }
}
