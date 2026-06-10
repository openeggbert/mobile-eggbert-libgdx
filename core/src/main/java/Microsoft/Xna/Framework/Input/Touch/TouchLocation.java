package Microsoft.Xna.Framework.Input.Touch;

import Microsoft.Xna.Framework.Vector2;

public class TouchLocation {
    private final int id;
    private final TouchLocationState state;
    private final Vector2 position;

    public TouchLocation(int id, TouchLocationState state, Vector2 position) {
        this.id = id;
        this.state = state;
        this.position = position;
    }

    public int Id() { return id; }
    public TouchLocationState State() { return state; }
    public Vector2 Position() { return position; }
}
