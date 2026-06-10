package Microsoft.Xna.Framework.Graphics;

public class BlendState {
    public static final BlendState AlphaBlend = new BlendState("AlphaBlend");
    public static final BlendState Opaque = new BlendState("Opaque");
    public static final BlendState NonPremultiplied = new BlendState("NonPremultiplied");

    private final String name;
    private BlendState(String name) { this.name = name; }

    @Override
    public String toString() { return name; }
}
