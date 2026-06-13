package com.openeggbert.mobileeggbert;

import Microsoft.Xna.Framework.Rectangle;

public class Misc {

    public static Rectangle RotateAdjust(Rectangle rect, double angle) {
        TinyPoint center = new TinyPoint(rect.width / 2, rect.height / 2);
        TinyPoint rotated = RotatePointRad(angle, center);
        int offsetX = rotated.X - center.X;
        int offsetY = rotated.Y - center.Y;
        return new Rectangle(rect.x - offsetX, rect.y - offsetY, rect.width, rect.height);
    }

    public static TinyPoint RotatePointRad(double angle, TinyPoint p) {
        return RotatePointRad(new TinyPoint(0, 0), angle, p);
    }

    public static TinyPoint RotatePointRad(TinyPoint center, double angle, TinyPoint point) {
        int relX = point.X - center.X;
        int relY = point.Y - center.Y;
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);
        TinyPoint result = new TinyPoint();
        result.X = (int)(relX * cos - relY * sin) + center.X;
        result.Y = (int)(relX * sin + relY * cos) + center.Y;
        return result;
    }

    public static double DegToRad(double angle) {
        return angle * Math.PI / 180.0;
    }

    public static int Approach(int actual, int target, int step) {
        if (actual < target) {
            actual = Math.min(actual + step, target);
        } else if (actual > target) {
            actual = Math.max(actual - step, target);
        }
        return actual;
    }

    public static int Speed(double speed, int max) {
        if (speed > 0.0) return Math.max((int)(speed * max), 1);
        if (speed < 0.0) return Math.min((int)(speed * max), -1);
        return 0;
    }

    public static TinyRect Inflate(TinyRect rect, int value) {
        TinyRect result = new TinyRect();
        result.Left = rect.Left - value;
        result.Right = rect.Right + value;
        result.Top = rect.Top - value;
        result.Bottom = rect.Bottom + value;
        return result;
    }

    public static boolean IsInside(TinyRect rect, TinyPoint p) {
        return p.X >= rect.Left && p.X <= rect.Right && p.Y >= rect.Top && p.Y <= rect.Bottom;
    }

    public static boolean IntersectRect(TinyRect[] dst, TinyRect src1, TinyRect src2) {
        dst[0] = new TinyRect();
        dst[0].Left = Math.max(src1.Left, src2.Left);
        dst[0].Right = Math.min(src1.Right, src2.Right);
        dst[0].Top = Math.max(src1.Top, src2.Top);
        dst[0].Bottom = Math.min(src1.Bottom, src2.Bottom);
        return !IsRectEmpty(dst[0]);
    }

    public static boolean UnionRect(TinyRect[] dst, TinyRect src1, TinyRect src2) {
        dst[0] = new TinyRect();
        dst[0].Left = Math.min(src1.Left, src2.Left);
        dst[0].Right = Math.max(src1.Right, src2.Right);
        dst[0].Top = Math.min(src1.Top, src2.Top);
        dst[0].Bottom = Math.max(src1.Bottom, src2.Bottom);
        return !IsRectEmpty(dst[0]);
    }

    private static boolean IsRectEmpty(TinyRect rect) {
        if (rect.Left < rect.Right) return rect.Top >= rect.Bottom;
        return true;
    }

    public static String ZeroPad(int n, int width) {
        String s = Integer.toString(n);
        while (s.length() < width) s = "0" + s;
        return s;
    }
}
