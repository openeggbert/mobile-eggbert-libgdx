package Microsoft.Xna.Framework;

public class Color {
    public float r, g, b, a;

    public static final Color White = new Color(1f, 1f, 1f, 1f);
    public static final Color Black = new Color(0f, 0f, 0f, 1f);
    public static final Color CornflowerBlue = new Color(0.392f, 0.584f, 0.929f, 1f);
    public static final Color TransparentBlack = new Color(0f, 0f, 0f, 0f);

    public Color(float r, float g, float b, float a) {
        this.r = r; this.g = g; this.b = b; this.a = a;
    }

    public static Color FromNonPremultiplied(int r, int g, int b, int a) {
        return new Color(r / 255f, g / 255f, b / 255f, a / 255f);
    }

    public com.badlogic.gdx.graphics.Color ToGdx() {
        return new com.badlogic.gdx.graphics.Color(r, g, b, a);
    }
}
