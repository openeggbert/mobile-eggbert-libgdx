package Microsoft.Xna.Framework;

import Microsoft.Xna.Framework.Graphics.GraphicsDevice;

public class GraphicsDeviceManager {
    private final GraphicsDevice graphicsDevice = new GraphicsDevice();
    public int PreferredBackBufferWidth = 640;
    public int PreferredBackBufferHeight = 480;
    public boolean IsFullScreen = false;

    public GraphicsDevice GraphicsDevice() {
        return graphicsDevice;
    }

    public void ApplyChanges() {
        // LibGDX handles window sizing via config at startup
    }
}
