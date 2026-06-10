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
        float delta = Gdx.graphics.getDeltaTime();
        totalTime += delta;
        GameTime gameTime = new GameTime(
            TimeSpan.FromSeconds(delta),
            TimeSpan.FromSeconds(totalTime)
        );
        Update(gameTime);
        Draw(gameTime);
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
