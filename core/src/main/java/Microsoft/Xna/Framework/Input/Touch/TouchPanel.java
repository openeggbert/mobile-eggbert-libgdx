package Microsoft.Xna.Framework.Input.Touch;

import com.badlogic.gdx.Gdx;
import Microsoft.Xna.Framework.Vector2;
import java.util.ArrayList;
import java.util.List;

public class TouchPanel {
    public static TouchPanelCapabilities GetCapabilities() {
        return new TouchPanelCapabilities(Gdx.input.isTouched());
    }

    public static List<TouchLocation> GetState() {
        List<TouchLocation> touches = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            if (Gdx.input.isTouched(i)) {
                Vector2 pos = new Vector2(Gdx.input.getX(i), Gdx.input.getY(i));
                TouchLocationState state = TouchLocationState.Pressed;
                touches.add(new TouchLocation(i, state, pos));
            }
        }
        return touches;
    }
}
