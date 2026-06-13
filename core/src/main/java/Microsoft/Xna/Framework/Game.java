package Microsoft.Xna.Framework;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import Microsoft.Xna.Framework.Content.ContentManager;
import Microsoft.Xna.Framework.Graphics.GraphicsDevice;
import System.TimeSpan;

public abstract class Game implements ApplicationListener {
    protected final GraphicsDeviceManager graphics;
    public final ContentManager Content;
    private double totalTime = 0;
    private double accumulator = 0;
    private static final double FIXED_STEP = 1.0 / 20.0; // XNA TargetElapsedTime = 50ms

    protected Game() {
        graphics = new GraphicsDeviceManager();
        Content = new ContentManager();
    }

    public GraphicsDevice GraphicsDevice() {
        return graphics.GraphicsDevice();
    }

    protected abstract void Initialize();
    protected abstract void LoadContent();
    protected abstract void Update(GameTime gameTime);
    protected abstract void Draw(GameTime gameTime);

    @Override
    public void create() {
        Initialize();
        LoadContent();
    }

    @Override
    public void render() {
        double delta = Math.min(Gdx.graphics.getDeltaTime(), 0.25);
        accumulator += delta;
        while (accumulator >= FIXED_STEP) {
            totalTime += FIXED_STEP;
            GameTime gameTime = new GameTime(
                TimeSpan.FromSeconds(FIXED_STEP),
                TimeSpan.FromSeconds(totalTime)
            );
            Update(gameTime);
            accumulator -= FIXED_STEP;
        }
        GameTime drawTime = new GameTime(
            TimeSpan.FromSeconds(FIXED_STEP),
            TimeSpan.FromSeconds(totalTime)
        );
        Draw(drawTime);
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {}
}
