package Microsoft.Xna.Framework;

public class Vector2 {
    public float x, y;

    public Vector2() {}
    public Vector2(float x, float y) { this.x = x; this.y = y; }

    public static final Vector2 Zero = new Vector2(0f, 0f);

    public com.badlogic.gdx.math.Vector2 ToGdx() {
        return new com.badlogic.gdx.math.Vector2(x, y);
    }
}
