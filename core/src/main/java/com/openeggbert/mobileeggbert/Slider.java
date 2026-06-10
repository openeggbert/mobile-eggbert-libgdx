package com.openeggbert.mobileeggbert;

public class Slider {
    private TinyPoint topLeftCorner;
    private double value;

    public Slider(TinyPoint topLeftCorner, double value) {
        this.topLeftCorner = topLeftCorner.Copy();
        this.value = value;
    }

    public TinyPoint GetTopLeftCorner() { return topLeftCorner; }
    public void SetTopLeftCorner(TinyPoint v) { this.topLeftCorner = v.Copy(); }

    public double GetValue() { return value; }
    public void SetValue(double v) { this.value = v; }

    private int GetPosLeft() { return topLeftCorner.X + 22; }
    private int GetPosRight() { return topLeftCorner.X + 248 - 22; }

    public void Draw(Pixmap pixmap) {
        TinyPoint dest = new TinyPoint(
            topLeftCorner.X - pixmap.Origin().X,
            topLeftCorner.Y - pixmap.Origin().Y
        );
        TinyRect rect = new TinyRect(0, 124, 0, 22);
        pixmap.DrawPart(5, dest, rect, 2.0);

        int num = (int)((GetPosRight() - GetPosLeft()) * value);
        int num2 = topLeftCorner.Y + 22;
        int num3 = 94;
        TinyRect rect2 = new TinyRect(
            GetPosLeft() + num - num3 / 2,
            GetPosLeft() + num + num3 / 2,
            num2 - num3 / 2,
            num2 + num3 / 2
        );
        pixmap.DrawIcon(14, 1, rect2, 1.0, false);

        TinyRect rect3 = new TinyRect(
            topLeftCorner.X - 65,
            topLeftCorner.X - 65 + 60,
            topLeftCorner.Y - 10,
            topLeftCorner.Y - 10 + 60
        );
        pixmap.DrawIcon(10, 37, rect3, 1.0, false);

        TinyRect rect4 = new TinyRect(
            topLeftCorner.X + 248 + 5,
            topLeftCorner.X + 248 + 5 + 60,
            topLeftCorner.Y - 10,
            topLeftCorner.Y - 10 + 60
        );
        pixmap.DrawIcon(10, 38, rect4, 1.0, false);
    }

    public boolean Move(TinyPoint pos) {
        TinyRect rect = new TinyRect(
            topLeftCorner.X - 50,
            topLeftCorner.X + 248 + 50,
            topLeftCorner.Y - 50,
            topLeftCorner.Y + 44 + 50
        );
        if (Misc.IsInside(rect, pos)) {
            double val = ((double)(pos.X - GetPosLeft())) / (GetPosRight() - GetPosLeft());
            val = Math.max(0.0, Math.min(1.0, val));
            if (value != val) {
                value = val;
                return true;
            }
        }
        return false;
    }
}
