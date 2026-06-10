package Microsoft.Xna.Framework;

public class Rectangle {
    public int x, y, width, height;

    public Rectangle() {}

    public Rectangle(int x, int y, int width, int height) {
        this.x = x; this.y = y; this.width = width; this.height = height;
    }

    public int Left() { return x; }
    public int Top() { return y; }
    public int Right() { return x + width; }
    public int Bottom() { return y + height; }
    public int Width() { return width; }
    public int Height() { return height; }

    public com.badlogic.gdx.math.Rectangle ToGdx() {
        return new com.badlogic.gdx.math.Rectangle(x, y, width, height);
    }
}
