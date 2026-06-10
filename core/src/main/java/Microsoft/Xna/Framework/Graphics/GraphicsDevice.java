package Microsoft.Xna.Framework.Graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import Microsoft.Xna.Framework.Color;

public class GraphicsDevice {
    private final Viewport viewport = new Viewport();

    public Viewport Viewport() {
        return viewport;
    }

    public void Clear(Color color) {
        Gdx.gl.glClearColor(color.r, color.g, color.b, color.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }
}
