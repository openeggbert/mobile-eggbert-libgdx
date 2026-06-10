package Microsoft.Xna.Framework.Graphics;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.Gdx;
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
        boolean flipX = (effects == SpriteEffects.FlipHorizontally);
        boolean flipY = (effects == SpriteEffects.FlipVertically);
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
