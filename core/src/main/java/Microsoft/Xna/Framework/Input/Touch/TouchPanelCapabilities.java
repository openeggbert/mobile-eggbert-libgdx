package Microsoft.Xna.Framework.Input.Touch;

public class TouchPanelCapabilities {
    private final boolean connected;

    public TouchPanelCapabilities(boolean connected) {
        this.connected = connected;
    }

    public boolean IsConnected() {
        return connected;
    }
}
