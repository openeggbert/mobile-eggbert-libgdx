package System.IO.IsolatedStorage;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;

public class IsolatedStorageFileStream {
    public static final int FILE_MODE_OPEN = 1;
    public static final int FILE_MODE_CREATE = 2;

    private static final String PREFS_NAME = "IsolatedStorage";

    private final String filename;
    private final FileHandle handle;
    private final int mode;
    private byte[] data;
    private int position;
    private byte[] writeBuffer;
    private int writeLength;

    public IsolatedStorageFileStream(String filename, int fileMode) throws IsolatedStorageException {
        this.filename = filename;
        this.mode = fileMode;
        if (Gdx.app.getType() == Application.ApplicationType.WebGL) {
            this.handle = null;
            if (fileMode == FILE_MODE_OPEN) {
                String hex = Gdx.app.getPreferences(PREFS_NAME).getString(filename, "");
                this.data = hexToBytes(hex);
                this.position = 0;
            } else {
                this.writeBuffer = new byte[65536];
                this.writeLength = 0;
            }
        } else {
            this.handle = Gdx.files.local(filename);
            if (fileMode == FILE_MODE_OPEN) {
                this.data = handle.readBytes();
                this.position = 0;
            } else {
                this.writeBuffer = new byte[65536];
                this.writeLength = 0;
            }
        }
    }

    public long Length() {
        return data != null ? data.length : 0;
    }

    public int Read(byte[] buffer, int offset, int count) throws IsolatedStorageException {
        if (data == null) return -1;
        int available = data.length - position;
        int toRead = Math.min(count, available);
        if (toRead <= 0) return -1;
        System.arraycopy(data, position, buffer, offset, toRead);
        position += toRead;
        return toRead;
    }

    public void Write(byte[] buffer, int offset, int count) throws IsolatedStorageException {
        if (writeLength + count > writeBuffer.length) {
            byte[] newBuf = new byte[(writeLength + count) * 2];
            System.arraycopy(writeBuffer, 0, newBuf, 0, writeLength);
            writeBuffer = newBuf;
        }
        System.arraycopy(buffer, offset, writeBuffer, writeLength, count);
        writeLength += count;
    }

    public void close() {
        if (mode == FILE_MODE_CREATE && writeBuffer != null) {
            byte[] toWrite = new byte[writeLength];
            System.arraycopy(writeBuffer, 0, toWrite, 0, writeLength);
            if (Gdx.app.getType() == Application.ApplicationType.WebGL) {
                Preferences prefs = Gdx.app.getPreferences(PREFS_NAME);
                prefs.putString(filename, bytesToHex(toWrite));
                prefs.flush();
            } else {
                handle.writeBytes(toWrite, false);
            }
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            int hi = (b >> 4) & 0xF;
            int lo = b & 0xF;
            sb.append((char)(hi < 10 ? '0' + hi : 'a' + hi - 10));
            sb.append((char)(lo < 10 ? '0' + lo : 'a' + lo - 10));
        }
        return sb.toString();
    }

    private static byte[] hexToBytes(String hex) {
        if (hex == null || hex.isEmpty()) return new byte[0];
        int len = hex.length();
        byte[] result = new byte[len / 2];
        for (int i = 0; i < len - 1; i += 2) {
            int hi = hexDigit(hex.charAt(i));
            int lo = hexDigit(hex.charAt(i + 1));
            result[i / 2] = (byte) ((hi << 4) | lo);
        }
        return result;
    }

    private static int hexDigit(char c) {
        if (c >= '0' && c <= '9') return c - '0';
        if (c >= 'a' && c <= 'f') return c - 'a' + 10;
        if (c >= 'A' && c <= 'F') return c - 'A' + 10;
        return 0;
    }
}
