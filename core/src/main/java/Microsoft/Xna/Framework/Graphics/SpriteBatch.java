package Microsoft.Xna.Framework.Graphics;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Matrix4;
import Microsoft.Xna.Framework.Color;
import Microsoft.Xna.Framework.Rectangle;
import Microsoft.Xna.Framework.Vector2;

public class SpriteBatch {
    private final com.badlogic.gdx.graphics.g2d.SpriteBatch gdxBatch;
    private boolean isInBatch = false;

    public SpriteBatch() {
        gdxBatch = new com.badlogic.gdx.graphics.g2d.SpriteBatch();
    }

    public void Begin(SpriteSortMode sortMode, BlendState blendState) {
        if (!isInBatch) {
            float w = Gdx.graphics.getWidth();
            float h = Gdx.graphics.getHeight();
            // Compute letterbox scale/offset so game space (0,0)-(640,480) fits centered in screen
            float scale = Math.min(w / 640f, h / 480f);
            float ox = (w - 640f * scale) / 2f;
            float oy = (h - 480f * scale) / 2f;
            // Ortho projection: maps game coords to letterboxed screen area (Y-down)
            gdxBatch.setProjectionMatrix(new Matrix4().setToOrtho(
                -ox / scale,           // left  (game x at screen left)
                (w - ox) / scale,      // right (game x at screen right)
                (h - oy) / scale,      // bottom (Y-down: larger = bottom)
                -oy / scale,           // top    (Y-down: smaller = top)
                0, 1
            ));
            gdxBatch.begin();
            isInBatch = true;
        }
    }

    public void End() {
        if (isInBatch) {
            gdxBatch.end();
            isInBatch = false;
        }
    }

    public void Draw(Texture2D texture, Rectangle destinationRectangle, Rectangle sourceRectangle, Color color) {
        TextureRegion region = new TextureRegion(
            texture.GetGdxTexture(),
            sourceRectangle.x, sourceRectangle.y,
            sourceRectangle.width, sourceRectangle.height
        );
        // With Y-down projection, LibGDX would draw textures upside-down; flip V to correct.
        region.flip(false, true);
        gdxBatch.setColor(color.r, color.g, color.b, color.a);
        gdxBatch.draw(region,
            destinationRectangle.x, destinationRectangle.y,
            destinationRectangle.width, destinationRectangle.height);
    }

    public void Draw(Texture2D texture, Rectangle destinationRectangle, Rectangle sourceRectangle,
                     Color color, float rotation, Vector2 origin, SpriteEffects effects, float layerDepth) {
        TextureRegion region = new TextureRegion(
            texture.GetGdxTexture(),
            sourceRectangle.x, sourceRectangle.y,
            sourceRectangle.width, sourceRectangle.height
        );
        // With Y-down projection, flip V; then apply any XNA SpriteEffects on top.
        boolean flipX = (effects == SpriteEffects.FlipHorizontally);
        boolean flipY = !(effects == SpriteEffects.FlipVertically); // inverted because of Y-down base flip
        region.flip(flipX, flipY);

        gdxBatch.setColor(color.r, color.g, color.b, color.a);
        gdxBatch.draw(region,
            destinationRectangle.x, destinationRectangle.y,
            origin.x, origin.y,
            destinationRectangle.width, destinationRectangle.height,
            1f, 1f,
            (float) Math.toDegrees(rotation));
    }

    public void Dispose() {
        gdxBatch.dispose();
    }

    public com.badlogic.gdx.graphics.g2d.SpriteBatch GetGdxBatch() {
        return gdxBatch;
    }
}
