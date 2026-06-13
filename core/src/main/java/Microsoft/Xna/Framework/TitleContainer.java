package Microsoft.Xna.Framework;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class TitleContainer {
    public static FileHandle GetFileHandle(String path) {
        return Gdx.files.internal(path);
    }
}
