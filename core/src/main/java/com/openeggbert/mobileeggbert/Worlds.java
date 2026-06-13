package com.openeggbert.mobileeggbert;

import com.badlogic.gdx.Gdx;
import Microsoft.Xna.Framework.TitleContainer;
import System.IO.IsolatedStorage.IsolatedStorageException;
import System.IO.IsolatedStorage.IsolatedStorageFile;
import System.IO.IsolatedStorage.IsolatedStorageFileStream;

public class Worlds {
    private static final StringBuilder output = new StringBuilder();

    private static final String GAME_DATA_FILENAME = "SpeedyBlupi";
    private static final String CURRENT_GAME_FILENAME = "CurrentGame";

    public static String[] ReadWorld(int gamer, int rank) {
        String worldFilename = "worlds/world" + Misc.ZeroPad(rank, 3) + ".txt";
        try {
            String text = TitleContainer.GetFileHandle(worldFilename).readString("UTF-8");
            if (text == null) return null;
            return text.split("\n");
        } catch (Exception e) {
            System.out.println("Error loading world " + worldFilename + ": " + e.getMessage());
            return null;
        }
    }

    public static boolean ReadGameData(byte[] data) {
        IsolatedStorageFile store = IsolatedStorageFile.GetUserStoreForApplication();
        if (store.FileExists(GAME_DATA_FILENAME)) {
            try {
                IsolatedStorageFileStream stream = store.OpenFile(GAME_DATA_FILENAME, IsolatedStorageFileStream.FILE_MODE_OPEN);
                int count = (int) Math.min(data.length, stream.Length());
                stream.Read(data, 0, count);
                stream.close();
                return true;
            } catch (IsolatedStorageException e) {
                return false;
            }
        }
        return false;
    }

    public static void WriteGameData(byte[] data) {
        try {
            IsolatedStorageFile store = IsolatedStorageFile.GetUserStoreForApplication();
            IsolatedStorageFileStream stream = store.OpenFile(GAME_DATA_FILENAME, IsolatedStorageFileStream.FILE_MODE_CREATE);
            stream.Write(data, 0, data.length);
            stream.close();
        } catch (IsolatedStorageException e) {
            System.out.println("Error writing game data: " + e.getMessage());
        }
    }

    public static void DeleteCurrentGame() {
        IsolatedStorageFile store = IsolatedStorageFile.GetUserStoreForApplication();
        try {
            store.DeleteFile(CURRENT_GAME_FILENAME);
        } catch (Exception ignored) {}
    }

    public static String ReadCurrentGame() {
        IsolatedStorageFile store = IsolatedStorageFile.GetUserStoreForApplication();
        if (store.FileExists(CURRENT_GAME_FILENAME)) {
            try {
                IsolatedStorageFileStream stream = store.OpenFile(CURRENT_GAME_FILENAME, IsolatedStorageFileStream.FILE_MODE_OPEN);
                byte[] bytes = new byte[(int) stream.Length()];
                stream.Read(bytes, 0, bytes.length);
                stream.close();
                return new String(bytes, "UTF-8");
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    public static void WriteCurrentGame(String data) {
        try {
            IsolatedStorageFile store = IsolatedStorageFile.GetUserStoreForApplication();
            IsolatedStorageFileStream stream = store.OpenFile(CURRENT_GAME_FILENAME, IsolatedStorageFileStream.FILE_MODE_CREATE);
            byte[] bytes = data.getBytes("UTF-8");
            stream.Write(bytes, 0, bytes.length);
            stream.close();
        } catch (Exception e) {
            System.out.println("Error writing current game: " + e.getMessage());
        }
    }

    public static void GetIntArrayField(String[] lines, String section, int rank, String name, int[] array) {
        for (String text : lines) {
            if (!text.startsWith(section + ":") || rank-- != 0) continue;
            int idx = text.indexOf(name + "=");
            if (idx == -1) break;
            idx += name.length() + 1;
            int end = text.indexOf(" ", idx);
            if (end == -1) break;
            String[] parts = text.substring(idx, end).split(",");
            for (int j = 0; j < parts.length; j++) {
                try { array[j] = Integer.parseInt(parts[j]); }
                catch (NumberFormatException e) { array[j] = 0; }
            }
        }
    }

    public static boolean GetBoolField(String[] lines, String section, int rank, String name) {
        for (String text : lines) {
            if (text.startsWith(section + ":") && rank-- == 0) {
                int idx = text.indexOf(name + "=");
                if (idx == -1) return false;
                idx += name.length() + 1;
                int end = text.indexOf(" ", idx);
                if (end == -1) return false;
                return Boolean.parseBoolean(text.substring(idx, end));
            }
        }
        return false;
    }

    public static int GetIntField(String[] lines, String section, int rank, String name) {
        for (String text : lines) {
            if (text.startsWith(section + ":") && rank-- == 0) {
                int idx = text.indexOf(name + "=");
                if (idx == -1) return 0;
                idx += name.length() + 1;
                int end = text.indexOf(" ", idx);
                if (end == -1) return 0;
                try { return Integer.parseInt(text.substring(idx, end)); }
                catch (NumberFormatException e) { return 0; }
            }
        }
        return 0;
    }

    public static double GetDoubleField(String[] lines, String section, int rank, String name) {
        for (String text : lines) {
            if (text.startsWith(section + ":") && rank-- == 0) {
                int idx = text.indexOf(name + "=");
                if (idx == -1) return 0.0;
                idx += name.length() + 1;
                int end = text.indexOf(" ", idx);
                if (end == -1) return 0.0;
                try { return Double.parseDouble(text.substring(idx, end)); }
                catch (NumberFormatException e) { return 0.0; }
            }
        }
        return 0.0;
    }

    public static TinyPoint GetPointField(String[] lines, String section, int rank, String name) {
        for (String text : lines) {
            if (text.startsWith(section + ":") && rank-- == 0) {
                int idx = text.indexOf(name + "=");
                if (idx == -1) return new TinyPoint();
                idx += name.length() + 1;
                int semi = text.indexOf(";", idx);
                int end = text.indexOf(" ", idx);
                if (semi == -1 || end == -1) return new TinyPoint();
                try {
                    int x = Integer.parseInt(text.substring(idx, semi));
                    int y = Integer.parseInt(text.substring(semi + 1, end));
                    return new TinyPoint(x, y);
                } catch (NumberFormatException e) { return new TinyPoint(); }
            }
        }
        return new TinyPoint();
    }

    public static Integer GetDecorField(String[] lines, String section, int x, int y) {
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].startsWith(section + ":")) {
                String text = lines[i + 1 + x];
                String[] parts = text.split(",", -1);
                if (y >= parts.length || parts[y] == null || parts[y].isEmpty()) return -1;
                try { return Integer.parseInt(parts[y]); }
                catch (NumberFormatException e) { return null; }
            }
        }
        return null;
    }

    public static void GetDoorsField(String[] lines, String section, int[] doors) {
        for (String text : lines) {
            if (!text.startsWith(section + ":")) continue;
            String[] parts = text.substring(section.length() + 2).split(",");
            for (int j = 0; j < parts.length; j++) {
                if (parts[j] == null || parts[j].isEmpty()) { doors[j] = 1; continue; }
                try { doors[j] = Integer.parseInt(parts[j]); }
                catch (NumberFormatException e) { /* keep existing */ }
            }
        }
    }

    public static void WriteClear() { output.setLength(0); }

    public static void WriteSection(String section) {
        output.append(section).append(": ");
    }

    public static void WriteIntArrayField(String name, int[] array) {
        output.append(name).append("=");
        for (int i = 0; i < array.length; i++) {
            if (array[i] != 0) output.append(array[i]);
            if (i < array.length - 1) output.append(",");
        }
        output.append(" ");
    }

    public static void WriteBoolField(String name, boolean n) {
        output.append(name).append("=").append(n).append(" ");
    }

    public static void WriteIntField(String name, int n) {
        output.append(name).append("=").append(n).append(" ");
    }

    public static void WriteDoubleField(String name, double n) {
        output.append(name).append("=").append(n).append(" ");
    }

    public static void WritePointField(String name, TinyPoint p) {
        output.append(name).append("=").append(p.X).append(";").append(p.Y).append(" ");
    }

    public static void WriteDecorField(int[] line) {
        for (int i = 0; i < line.length; i++) {
            if (line[i] != -1) output.append(line[i]);
            if (i < line.length - 1) output.append(",");
        }
        output.append("\n");
    }

    public static void WriteDoorsField(int[] doors) {
        for (int i = 0; i < doors.length; i++) {
            if (doors[i] != 1) output.append(doors[i]);
            if (i < doors.length - 1) output.append(",");
        }
        output.append("\n");
    }

    public static void WriteEndSection() { output.append("\n"); }

    public static String GetWriteString() { return output.toString(); }
}
