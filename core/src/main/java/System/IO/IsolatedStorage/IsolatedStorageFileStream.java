package System.IO.IsolatedStorage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import java.io.*;

public class IsolatedStorageFileStream implements Closeable {
    public static final int FILE_MODE_OPEN = 1;
    public static final int FILE_MODE_CREATE = 2;

    private final FileHandle handle;
    private InputStream inputStream;
    private OutputStream outputStream;
    private final int mode;

    public IsolatedStorageFileStream(String filename, int fileMode) throws IOException {
        this.handle = Gdx.files.local(filename);
        this.mode = fileMode;
        if (fileMode == FILE_MODE_OPEN) {
            this.inputStream = handle.read();
        } else {
            this.outputStream = handle.write(false);
        }
    }

    public long Length() {
        return handle.length();
    }

    public int Read(byte[] buffer, int offset, int count) throws IOException {
        return inputStream.read(buffer, offset, count);
    }

    public void Write(byte[] buffer, int offset, int count) throws IOException {
        outputStream.write(buffer, offset, count);
    }

    @Override
    public void close() throws IOException {
        if (inputStream != null) inputStream.close();
        if (outputStream != null) outputStream.close();
    }
}
