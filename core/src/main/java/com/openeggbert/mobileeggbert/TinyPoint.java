package com.openeggbert.mobileeggbert;

public class TinyPoint {
    public int X;
    public int Y;

    public TinyPoint() {}

    public TinyPoint(int x, int y) {
        X = x;
        Y = y;
    }

    public TinyPoint Copy() {
        return new TinyPoint(X, Y);
    }

    @Override
    public String toString() {
        return X + ";" + Y;
    }
}
