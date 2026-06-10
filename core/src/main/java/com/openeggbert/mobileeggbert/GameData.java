package com.openeggbert.mobileeggbert;

public class GameData {
    private static final int SaveHeaderLength = 10;
    private static final int GamerHeaderLength = 10;
    private static final int DoorsLength = 200;
    private static final int GamerLength = GamerHeaderLength + DoorsLength;
    private static final int MaxGamer = 3;
    private static final int TotalLength = SaveHeaderLength + GamerLength * MaxGamer;

    private final byte[] data;

    public GameData() {
        data = new byte[TotalLength];
        Initialize();
    }

    public int SelectedGamer() { return data[2] & 0xFF; }
    public void SetSelectedGamer(int v) { data[2] = (byte) v; }

    public boolean Sounds() { return data[3] == 1; }
    public void SetSounds(boolean v) { data[3] = (byte)(v ? 1 : 0); }

    public boolean JumpRight() { return data[4] == 1; }
    public void SetJumpRight(boolean v) { data[4] = (byte)(v ? 1 : 0); }

    public boolean AutoZoom() { return data[5] == 1; }
    public void SetAutoZoom(boolean v) { data[5] = (byte)(v ? 1 : 0); }

    public boolean AccelActive() { return data[6] == 1; }
    public void SetAccelActive(boolean v) { data[6] = (byte)(v ? 1 : 0); }

    public double AccelSensitivity() { return (data[7] & 0xFF) / 100.0; }
    public void SetAccelSensitivity(double v) {
        v = Math.max(0.0, Math.min(1.0, v));
        data[7] = (byte)(v * 100.0);
    }

    public int NbVies() { return data[GamerOffset()] & 0xFF; }
    public void SetNbVies(int v) { data[GamerOffset()] = (byte) v; }

    public int LastWorld() { return data[GamerOffset() + 1] & 0xFF; }
    public void SetLastWorld(int v) { data[GamerOffset() + 1] = (byte) v; }

    private int GamerOffset() {
        return GamerOffset(SelectedGamer());
    }

    private int GamerOffset(int gamer) {
        return SaveHeaderLength + GamerLength * gamer;
    }

    public void Read() {
        Worlds.ReadGameData(data);
    }

    public void Write() {
        Worlds.WriteGameData(data);
    }

    public void Reset() {
        Initialize(SelectedGamer());
    }

    public void GetDoors(int[] doors) {
        int offset = GamerOffset() + GamerHeaderLength;
        for (int i = 0; i < DoorsLength; i++) {
            doors[i] = data[offset + i] & 0xFF;
        }
    }

    public void SetDoors(int[] doors) {
        int offset = GamerOffset() + GamerHeaderLength;
        for (int i = 0; i < DoorsLength; i++) {
            data[offset + i] = (byte) doors[i];
        }
    }

    public void GetGamerInfo(int gamer, int[] nbVies, int[] mainDoors, int[] secondaryDoors) {
        nbVies[0] = data[GamerOffset(gamer)] & 0xFF;
        secondaryDoors[0] = 0;
        for (int i = 0; i < 180; i++) {
            if ((data[GamerOffset(gamer) + GamerHeaderLength + i] & 0xFF) == 1) secondaryDoors[0]++;
        }
        mainDoors[0] = 0;
        for (int j = 180; j < 200; j++) {
            if ((data[GamerOffset(gamer) + GamerHeaderLength + j] & 0xFF) == 1) mainDoors[0]++;
        }
    }

    private void Initialize() {
        data[0] = 1; data[1] = 1; data[2] = 0; data[3] = 1;
        data[4] = 1; data[5] = 1; data[6] = 0; data[7] = 50;
        for (int i = 0; i < MaxGamer; i++) Initialize(i);
    }

    private void Initialize(int gamer) {
        data[GamerOffset(gamer)] = 3;
        data[GamerOffset(gamer) + 1] = 1;
        for (int i = 0; i < DoorsLength; i++) {
            data[GamerOffset(gamer) + GamerHeaderLength + i] = 0;
        }
    }

    public byte[] Data() { return data; }
}
