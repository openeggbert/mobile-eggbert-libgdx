package System.IO.IsolatedStorage;

import com.badlogic.gdx.Gdx;
import java.io.IOException;

public class IsolatedStorageFile {
    private static final IsolatedStorageFile INSTANCE = new IsolatedStorageFile();

    private IsolatedStorageFile() {}

    public static IsolatedStorageFile GetUserStoreForApplication() {
        return INSTANCE;
    }

    public static IsolatedStorageFile GetUserStoreForAssembly() {
        return INSTANCE;
    }

    public boolean FileExists(String filename) {
        return Gdx.files.local(filename).exists();
    }

    public IsolatedStorageFileStream OpenFile(String filename, int fileMode) throws IsolatedStorageException {
        try {
            return new IsolatedStorageFileStream(filename, fileMode);
        } catch (IOException e) {
            throw new IsolatedStorageException(e.getMessage());
        }
    }

    public void DeleteFile(String filename) {
        Gdx.files.local(filename).delete();
    }
}
