package Microsoft.Xna.Framework.Graphics;

import com.badlogic.gdx.graphics.Texture;
import Microsoft.Xna.Framework.Rectangle;

public class Texture2D {
    private final Texture gdxTexture;

    public Texture2D(Texture gdxTexture) {
        this.gdxTexture = gdxTexture;
    }

    public Texture GetGdxTexture() {
        return gdxTexture;
    }

    public Rectangle Bounds() {
        return new Rectangle(0, 0, gdxTexture.getWidth(), gdxTexture.getHeight());
    }

    public int Width() {
        return gdxTexture.getWidth();
    }

    public int Height() {
        return gdxTexture.getHeight();
    }

    public void Dispose() {
        gdxTexture.dispose();
    }
}
