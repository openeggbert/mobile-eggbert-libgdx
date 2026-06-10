package Microsoft.Xna.Framework;

import System.TimeSpan;

public class GameTime {
    private final TimeSpan elapsed;
    private final TimeSpan total;

    public GameTime(TimeSpan elapsed, TimeSpan total) {
        this.elapsed = elapsed;
        this.total = total;
    }

    public TimeSpan ElapsedGameTime() {
        return elapsed;
    }

    public TimeSpan TotalGameTime() {
        return total;
    }
}
