package System.IO.IsolatedStorage;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class IsolatedStorageFile {
    private static final IsolatedStorageFile INSTANCE = new IsolatedStorageFile();
    private static final String PREFS_NAME = "IsolatedStorage";

    private IsolatedStorageFile() {}

    public static IsolatedStorageFile GetUserStoreForApplication() {
        return INSTANCE;
    }

    public static IsolatedStorageFile GetUserStoreForAssembly() {
        return INSTANCE;
    }

    public boolean FileExists(String filename) {
        if (Gdx.app.getType() == Application.ApplicationType.WebGL) {
            return Gdx.app.getPreferences(PREFS_NAME).contains(filename);
        }
        return Gdx.files.local(filename).exists();
    }

    public IsolatedStorageFileStream OpenFile(String filename, int fileMode) throws IsolatedStorageException {
        return new IsolatedStorageFileStream(filename, fileMode);
    }

    public void DeleteFile(String filename) {
        if (Gdx.app.getType() == Application.ApplicationType.WebGL) {
            Preferences prefs = Gdx.app.getPreferences(PREFS_NAME);
            prefs.remove(filename);
            prefs.flush();
            return;
        }
        Gdx.files.local(filename).delete();
    }
}
