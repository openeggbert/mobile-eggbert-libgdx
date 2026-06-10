package com.openeggbert.mobileeggbert;

import Microsoft.Xna.Framework.Rectangle;

public class TinyRect {
    public int Left;
    public int Right;
    public int Top;
    public int Bottom;

    public TinyRect() {}

    public TinyRect(int left, int right, int top, int bottom) {
        Left = left;
        Right = right;
        Top = top;
        Bottom = bottom;
    }

    public TinyRect(TinyPoint point) {
        Left = point.X;
        Right = point.X;
        Top = point.Y;
        Bottom = point.Y;
    }

    public int Width() {
        return Right - Left;
    }

    public int Height() {
        return Bottom - Top;
    }

    public TinyRect Copy() {
        return new TinyRect(Left, Right, Top, Bottom);
    }

    public Rectangle ToXnaRectangle() {
        return new Rectangle(Left, Top, Width(), Height());
    }

    @Override
    public String toString() {
        return Left + ";" + Top + ";" + Right + ";" + Bottom;
    }
}
