package Microsoft.Xna.Framework;

import com.badlogic.gdx.Gdx;
import java.io.InputStream;

public class TitleContainer {
    public static InputStream OpenStream(String path) {
        return Gdx.files.internal(path).read();
    }
}
