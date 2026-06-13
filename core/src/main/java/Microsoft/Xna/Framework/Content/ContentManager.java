package Microsoft.Xna.Framework.Content;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import Microsoft.Xna.Framework.Audio.SoundEffect;
import Microsoft.Xna.Framework.Graphics.Texture2D;

public class ContentManager {
    @SuppressWarnings("unchecked")
    public <T> T Load(Class<T> type, String assetName) {
        if (type == Texture2D.class) {
            String path = assetName + ".png";
            return (T) new Texture2D(new Texture(Gdx.files.internal(path)));
        }
        if (type == SoundEffect.class) {
            String path = assetName + ".wav";
            return (T) new SoundEffect(Gdx.audio.newSound(Gdx.files.internal(path)));
        }
        throw new UnsupportedOperationException("ContentManager.Load: unsupported type " + type.getName());
    }
}
