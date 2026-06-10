package com.openeggbert.mobileeggbert;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import Microsoft.Xna.Framework.Input.Touch.TouchLocation;
import Microsoft.Xna.Framework.Input.Touch.TouchLocationState;
import Microsoft.Xna.Framework.Input.Touch.TouchPanel;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles all input (touch, mouse, keyboard) and maps it to game actions.
 *
 * Ported from InputPad.cs (C# / Windows Phone XNA) to Java / LibGDX.
 * The Accelerometer sensor is stubbed out — it always reports zero because
 * LibGDX accelerometer access requires the Android back-end and is not
 * available on desktop.
 */
public class InputPad {

    // -----------------------------------------------------------------------
    // Stub: Accelerometer (Windows Phone sensor — not available here)
    // -----------------------------------------------------------------------

    /**
     * Minimal stub that replaces the Windows Phone {@code Accelerometer} sensor.
     * Always reports zero acceleration so the sensor-based movement path is
     * functionally disabled without crashing.
     */
    private static class AccelerometerStub {
        private boolean started = false;

        public void Start() {
            started = true;
        }

        public void Stop() {
            started = false;
        }

        public boolean IsStarted() {
            return started;
        }
    }

    // -----------------------------------------------------------------------
    // Constants
    // -----------------------------------------------------------------------

    private static final int padRadius = 140;

    // -----------------------------------------------------------------------
    // Dependencies
    // -----------------------------------------------------------------------

    private final Game1 game1;
    private final Decor decor;
    private final Pixmap pixmap;
    private final Sound sound;
    private final GameData gameData;

    // -----------------------------------------------------------------------
    // State
    // -----------------------------------------------------------------------

    private final List<Def.ButtonGlyph> pressedGlyphs;
    private final AccelerometerStub accelSensor;
    private final Slider accelSlider;

    private boolean padPressed;
    private boolean showCheatMenu;
    private TinyPoint padTouchPos;
    private Def.ButtonGlyph lastButtonDown;
    private Def.ButtonGlyph buttonPressed;
    private int touchOrClickCount;
    private boolean accelStarted;
    private boolean accelActive;
    private double accelSpeedX;
    private boolean accelLastState;
    private boolean accelWaitZero;
    private int mission;

    // -----------------------------------------------------------------------
    // Properties (C# auto-properties → Java getters/setters)
    // -----------------------------------------------------------------------

    private Def.Phase phase;

    public Def.Phase Phase() { return phase; }
    public void SetPhase(Def.Phase phase) { this.phase = phase; }

    private int selectedGamer;

    public int GetSelectedGamer() { return selectedGamer; }
    public void SetSelectedGamer(int selectedGamer) { this.selectedGamer = selectedGamer; }

    private TinyPoint pixmapOrigin = new TinyPoint();

    public TinyPoint PixmapOrigin() { return pixmapOrigin; }
    public void SetPixmapOrigin(TinyPoint pixmapOrigin) { this.pixmapOrigin = pixmapOrigin; }

    /** Read-once: returns the last completed button press and resets the field. */
    public Def.ButtonGlyph ButtonPressed() {
        Def.ButtonGlyph result = buttonPressed;
        buttonPressed = Def.ButtonGlyph.None;
        return result;
    }

    public int GetTotalTouchOrClick() {
        return touchOrClickCount;
    }

    public boolean GetShowCheatMenu() { return showCheatMenu; }
    public void SetShowCheatMenu(boolean showCheatMenu) { this.showCheatMenu = showCheatMenu; }

    // -----------------------------------------------------------------------
    // Constructor
    // -----------------------------------------------------------------------

    public InputPad(Game1 game1, Decor decor, Pixmap pixmap, Sound sound, GameData gameData) {
        this.game1 = game1;
        this.decor = decor;
        this.pixmap = pixmap;
        this.sound = sound;
        this.gameData = gameData;
        pressedGlyphs = new ArrayList<>();
        accelSensor = new AccelerometerStub();
        accelSlider = new Slider(new TinyPoint(320, 400), gameData.AccelSensitivity());
        lastButtonDown = Def.ButtonGlyph.None;
        buttonPressed = Def.ButtonGlyph.None;
        padTouchPos = new TinyPoint();
        phase = Def.Phase.None;
        pixmapOrigin = new TinyPoint();
    }

    // -----------------------------------------------------------------------
    // Public API
    // -----------------------------------------------------------------------

    public void StartMission(int mission) {
        this.mission = mission;
        accelWaitZero = true;
    }

    public void Update() {
        pressedGlyphs.clear();

        // Sync accelerometer on/off with game-data setting
        if (accelActive != gameData.AccelActive()) {
            accelActive = gameData.AccelActive();
            if (accelActive) {
                StartAccel();
            } else {
                StopAccel();
            }
        }

        double horizontalChange = 0.0;
        double verticalChange = 0.0;
        int keyPress = 0;
        padPressed = false;
        Def.ButtonGlyph buttonGlyph = Def.ButtonGlyph.None;

        // ---- Touch input ----
        List<TinyPoint> touchesOrClicks = new ArrayList<>();
        if (Env.IMPL.IsNotKNI()) {
            List<TouchLocation> touches = TouchPanel.GetState();
            touchOrClickCount = touches.size();
            for (TouchLocation item : touches) {
                if (item.State() == TouchLocationState.Pressed
                        || item.State() == TouchLocationState.Moved) {
                    TinyPoint touchPress = new TinyPoint(
                            (int) item.Position().x,
                            (int) item.Position().y);
                    touchesOrClicks.add(touchPress);
                }
            }
        }

        // ---- Mouse input ----
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            touchOrClickCount++;
            TinyPoint mouseClick = new TinyPoint(Gdx.input.getX(), Gdx.input.getY());
            touchesOrClicks.add(mouseClick);
        }

        // ---- Screen-ratio coordinate rescaling ----
        float screenWidth  = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        float screenRatio  = screenWidth / screenHeight;

        if ((Env.PLATFORM.IsAndroid() && screenRatio > 1.3333333333333333)
                || (Env.IMPL.IsKNI())) {
            for (int i = 0; i < touchesOrClicks.size(); i++) {
                TinyPoint touchOrClick = touchesOrClicks.get(i);
                if (touchOrClick.X == -1) continue;

                float originalX = touchOrClick.X;
                float originalY = touchOrClick.Y;
                float heightRatio  = 480f / screenHeight;
                float widthRatio   = 640f / screenWidth;

                DDebug.WriteLine("-----");
                DDebug.WriteLine("originalX=" + originalX);
                DDebug.WriteLine("originalY=" + originalY);
                DDebug.WriteLine("heightRatio=" + heightRatio);
                DDebug.WriteLine("widthRatio=" + widthRatio);
                DDebug.WriteLine("widthHeightRatio=" + (screenWidth / screenHeight));

                if (screenHeight > 480) {
                    touchOrClick.X = (int)(originalX * heightRatio);
                    touchOrClick.Y = (int)(originalY * heightRatio);
                    touchesOrClicks.set(i, touchOrClick);
                }

                DDebug.WriteLine("new X" + touchOrClick.X);
                DDebug.WriteLine("new Y" + touchOrClick.Y);
            }
        }

        // ---- Keyboard input — encode as sentinel TinyPoint(X=-1, Y=keyCode) ----
        int[] keysToCheck = {
            Input.Keys.CONTROL_LEFT,
            Input.Keys.UP,
            Input.Keys.RIGHT,
            Input.Keys.DOWN,
            Input.Keys.LEFT,
            Input.Keys.SPACE,
            Input.Keys.ESCAPE
        };
        for (int key : keysToCheck) {
            if (Gdx.input.isKeyPressed(key)) {
                touchesOrClicks.add(new TinyPoint(-1, key));
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.F11)) {
            game1.ToggleFullScreen();
            DDebug.WriteLine("F11 was pressed.");
        }

        // ---- Process each touch / click / key event ----
        boolean keyPressedUp    = false;
        boolean keyPressedDown  = false;
        boolean keyPressedLeft  = false;
        boolean keyPressedRight = false;

        for (TinyPoint touchOrClickItem : touchesOrClicks) {
            boolean keyboardPressed = (touchOrClickItem.X == -1);
            int keyPressed = keyboardPressed ? touchOrClickItem.Y : -1;

            if (keyPressed == Input.Keys.UP)    keyPressedUp    = true;
            if (keyPressed == Input.Keys.DOWN)  keyPressedDown  = true;
            if (keyPressed == Input.Keys.LEFT)  keyPressedLeft  = true;
            if (keyPressed == Input.Keys.RIGHT) keyPressedRight = true;

            {
                TinyPoint touchOrClick = keyboardPressed ? new TinyPoint(1, 1) : touchOrClickItem;
                if (!accelStarted && Misc.IsInside(GetPadBounds(GetPadCenter(), padRadius), touchOrClick)) {
                    padPressed = true;
                    padTouchPos = touchOrClick;
                }
                if (keyPressedUp || keyPressedDown || keyPressedLeft || keyPressedRight) {
                    padPressed = true;
                }
                DDebug.WriteLine("padPressed=" + padPressed);

                Def.ButtonGlyph pressedGlyph = ButtonDetect(touchOrClick);
                DDebug.WriteLine("buttonGlyph2 =" + pressedGlyph);
                if (pressedGlyph != Def.ButtonGlyph.None) {
                    pressedGlyphs.add(pressedGlyph);
                }

                // Map keyboard keys to logical button glyphs
                if (keyboardPressed) {
                    Def.ButtonGlyph keyGlyph = Def.ButtonGlyph.None;
                    if (keyPressed == Input.Keys.CONTROL_LEFT) keyGlyph = Def.ButtonGlyph.PlayJump;
                    else if (keyPressed == Input.Keys.SPACE)   keyGlyph = Def.ButtonGlyph.PlayAction;
                    else if (keyPressed == Input.Keys.ESCAPE)  keyGlyph = Def.ButtonGlyph.PlayPause;
                    if (keyGlyph != Def.ButtonGlyph.None) {
                        pressedGlyph = keyGlyph;
                        pressedGlyphs.add(pressedGlyph);
                    }
                }

                if ((phase == Def.Phase.MainSetup || phase == Def.Phase.PlaySetup)
                        && accelSlider.Move(touchOrClick)) {
                    gameData.SetAccelSensitivity(accelSlider.GetValue());
                }

                switch (pressedGlyph) {
                    case PlayJump:
                        DDebug.WriteLine("Jumping detected");
                        accelWaitZero = false;
                        keyPress |= 1;
                        break;
                    case PlayDown:
                        accelWaitZero = false;
                        keyPress |= 4;
                        break;
                    case InitGamerA:
                    case InitGamerB:
                    case InitGamerC:
                    case InitSetup:
                    case InitPlay:
                    case InitBuy:
                    case InitRanking:
                    case WinLostReturn:
                    case TrialBuy:
                    case TrialCancel:
                    case SetupSounds:
                    case SetupJump:
                    case SetupZoom:
                    case SetupAccel:
                    case SetupReset:
                    case SetupReturn:
                    case PauseMenu:
                    case PauseBack:
                    case PauseSetup:
                    case PauseRestart:
                    case PauseContinue:
                    case PlayPause:
                    case PlayAction:
                    case ResumeMenu:
                    case ResumeContinue:
                    case RankingContinue:
                    case Cheat11:
                    case Cheat12:
                    case Cheat21:
                    case Cheat22:
                    case Cheat31:
                    case Cheat32:
                    case Cheat1:
                    case Cheat2:
                    case Cheat3:
                    case Cheat4:
                    case Cheat5:
                    case Cheat6:
                    case Cheat7:
                    case Cheat8:
                    case Cheat9:
                        accelWaitZero = false;
                        buttonGlyph = pressedGlyph;
                        showCheatMenu = false;
                        break;
                    default:
                        break;
                }
            }
        }

        // Play click sound when a new button is first pressed (excluding action/cheat buttons)
        if (buttonGlyph != Def.ButtonGlyph.None
                && buttonGlyph != Def.ButtonGlyph.PlayAction
                && buttonGlyph != Def.ButtonGlyph.Cheat11
                && buttonGlyph != Def.ButtonGlyph.Cheat12
                && buttonGlyph != Def.ButtonGlyph.Cheat21
                && buttonGlyph != Def.ButtonGlyph.Cheat22
                && buttonGlyph != Def.ButtonGlyph.Cheat31
                && buttonGlyph != Def.ButtonGlyph.Cheat32
                && lastButtonDown == Def.ButtonGlyph.None) {
            TinyPoint pos = new TinyPoint(320, 240);
            sound.PlayImage(0, pos);
        }

        // A button is "pressed" (fired) when a previously held button is released
        if (buttonGlyph == Def.ButtonGlyph.None && lastButtonDown != Def.ButtonGlyph.None) {
            buttonPressed = lastButtonDown;
        }
        lastButtonDown = buttonGlyph;

        // Compute pad directional input
        if (padPressed) {
            DDebug.WriteLine("PadCenter.X=" + GetPadCenter().X);
            DDebug.WriteLine("PadCenter.Y=" + GetPadCenter().Y);
            DDebug.WriteLine("padTouchPos.X=" + padTouchPos.X);
            DDebug.WriteLine("padTouchPos.Y=" + padTouchPos.Y);
            DDebug.WriteLine("keyPressedUp="    + keyPressedUp);
            DDebug.WriteLine("keyPressedDown="  + keyPressedDown);
            DDebug.WriteLine("keyPressedLeft="  + keyPressedLeft);
            DDebug.WriteLine("keyPressedRight=" + keyPressedRight);

            // Override padTouchPos when arrow keys are pressed
            if (keyPressedUp) {
                padTouchPos.Y = GetPadCenter().Y - 30;
                padTouchPos.X = GetPadCenter().X;
                if (keyPressedLeft)  padTouchPos.X = GetPadCenter().X - 30;
                if (keyPressedRight) padTouchPos.X = GetPadCenter().X + 30;
            }
            if (keyPressedDown) {
                padTouchPos.Y = GetPadCenter().Y + 30;
                padTouchPos.X = GetPadCenter().X;
                if (keyPressedLeft)  padTouchPos.X = GetPadCenter().X - 30;
                if (keyPressedRight) padTouchPos.X = GetPadCenter().X + 30;
            }
            if (keyPressedLeft) {
                padTouchPos.X = GetPadCenter().X - 30;
                padTouchPos.Y = GetPadCenter().Y;
                if (keyPressedUp)   padTouchPos.Y = GetPadCenter().Y - 30;
                if (keyPressedDown) padTouchPos.Y = GetPadCenter().Y + 30;
            }
            if (keyPressedRight) {
                padTouchPos.X = GetPadCenter().X + 30;
                padTouchPos.Y = GetPadCenter().Y;
                if (keyPressedUp)   padTouchPos.Y = GetPadCenter().Y - 30;
                if (keyPressedDown) padTouchPos.Y = GetPadCenter().Y + 30;
            }

            double horizontalPosition = padTouchPos.X - GetPadCenter().X;
            double verticalPosition   = padTouchPos.Y - GetPadCenter().Y;

            if (horizontalPosition > 20.0)  { horizontalChange += 1.0; DDebug.WriteLine(" horizontalChange += 1.0;"); }
            if (horizontalPosition < -20.0) { horizontalChange -= 1.0; DDebug.WriteLine(" horizontalChange -= 1.0;"); }
            if (verticalPosition   > 20.0)  { verticalChange   += 1.0; DDebug.WriteLine(" verticalPosition += 1.0;"); }
            if (verticalPosition   < -20.0) { verticalChange   -= 1.0; DDebug.WriteLine(" verticalPosition -= 1.0;"); }
        }

        if (accelStarted) {
            horizontalChange = accelSpeedX;
            verticalChange   = 0.0;
            if ((keyPress & 4) != 0) {
                verticalChange = 1.0;
            }
        }

        decor.SetSpeedX(horizontalChange);
        decor.SetSpeedY(verticalChange);
        decor.KeyChange(keyPress);
    }

    public void Draw() {
        if (!accelStarted && phase == Def.Phase.Play) {
            pixmap.DrawIcon(14, 0, GetPadBounds(GetPadCenter(), padRadius / 2), 1.0, false);
            TinyPoint center = padPressed ? padTouchPos : GetPadCenter();
            pixmap.DrawIcon(14, 1, GetPadBounds(center, padRadius / 2), 1.0, false);
        }
        for (Def.ButtonGlyph bg : GetButtonGlyphs()) {
            boolean pressed  = pressedGlyphs.contains(bg);
            boolean selected = false;
            if (bg.ordinal() >= Def.ButtonGlyph.InitGamerA.ordinal()
                    && bg.ordinal() <= Def.ButtonGlyph.InitGamerC.ordinal()) {
                int gamerIndex = bg.ordinal() - Def.ButtonGlyph.InitGamerA.ordinal();
                selected = (gamerIndex == gameData.SelectedGamer());
            }
            if (bg == Def.ButtonGlyph.SetupSounds) selected = gameData.Sounds();
            if (bg == Def.ButtonGlyph.SetupJump)   selected = gameData.JumpRight();
            if (bg == Def.ButtonGlyph.SetupZoom)   selected = gameData.AutoZoom();
            if (bg == Def.ButtonGlyph.SetupAccel)  selected = gameData.AccelActive();
            pixmap.DrawInputButton(GetButtonRect(bg), bg, pressed, selected);
        }
        if ((phase == Def.Phase.MainSetup || phase == Def.Phase.PlaySetup)
                && gameData.AccelActive()) {
            accelSlider.Draw(pixmap);
        }
    }

    /**
     * Returns the screen rectangle of the given button glyph, used both for
     * hit-testing input and for rendering.
     *
     * @param glyph the button glyph to query
     * @return the bounding {@link TinyRect} in screen / draw-bounds coordinates
     */
    public TinyRect GetButtonRect(Def.ButtonGlyph glyph) {
        TinyRect drawBounds        = pixmap.DrawBounds();
        double drawBoundsWidth     = drawBounds.Right - drawBounds.Left;
        double drawBoundsHeight    = drawBounds.Bottom - drawBounds.Top;
        double buttonSizeFactor1   = drawBoundsHeight / 5.0;
        double buttonSizeFactor2   = drawBoundsHeight * 140.0 / 480.0;
        double cheatButtonSizeFactor = drawBoundsHeight / 3.5;

        // Cheat overlay buttons (small 80px grid at top-left)
        if (glyph.ordinal() >= Def.ButtonGlyph.Cheat1.ordinal()
                && glyph.ordinal() <= Def.ButtonGlyph.Cheat9.ordinal()) {
            int cheatNumber = glyph.ordinal() - Def.ButtonGlyph.Cheat1.ordinal();
            TinyRect result = new TinyRect();
            result.Left   = 80 * cheatNumber;
            result.Right  = 80 * (cheatNumber + 1);
            result.Top    = 0;
            result.Bottom = 80;
            return result;
        }

        int leftXForButtonsInLeftColumn  = (int)(20.0 + buttonSizeFactor2 * 0.0);
        int rightXForButtonsInLeftColumn = (int)(20.0 + buttonSizeFactor2 * 0.5);

        switch (glyph) {
            case InitGamerA: {
                TinyRect r = new TinyRect();
                r.Left   = leftXForButtonsInLeftColumn;
                r.Right  = rightXForButtonsInLeftColumn;
                r.Top    = (int)(drawBoundsHeight - 20.0 - buttonSizeFactor2 * 2.1);
                r.Bottom = (int)(drawBoundsHeight - 20.0 - buttonSizeFactor2 * 1.6);
                return r;
            }
            case InitGamerB: {
                TinyRect r = new TinyRect();
                r.Left   = leftXForButtonsInLeftColumn;
                r.Right  = rightXForButtonsInLeftColumn;
                r.Top    = (int)(drawBoundsHeight - 20.0 - buttonSizeFactor2 * 1.6);
                r.Bottom = (int)(drawBoundsHeight - 20.0 - buttonSizeFactor2 * 1.1);
                return r;
            }
            case InitGamerC: {
                TinyRect r = new TinyRect();
                r.Left   = leftXForButtonsInLeftColumn;
                r.Right  = rightXForButtonsInLeftColumn;
                r.Top    = (int)(drawBoundsHeight - 20.0 - buttonSizeFactor2 * 1.1);
                r.Bottom = (int)(drawBoundsHeight - 20.0 - buttonSizeFactor2 * 0.6);
                return r;
            }
            case InitSetup: {
                TinyRect r = new TinyRect();
                r.Left   = leftXForButtonsInLeftColumn;
                r.Right  = rightXForButtonsInLeftColumn;
                r.Top    = (int)(drawBoundsHeight - 20.0 - buttonSizeFactor2 * 0.5);
                r.Bottom = (int)(drawBoundsHeight - 20.0 - buttonSizeFactor2 * 0.0);
                return r;
            }
            case InitPlay: {
                TinyRect r = new TinyRect();
                r.Left   = (int)(drawBoundsWidth - 20.0 - buttonSizeFactor2 * 1.0);
                r.Right  = (int)(drawBoundsWidth - 20.0 - buttonSizeFactor2 * 0.0);
                r.Top    = (int)(drawBoundsHeight - 40.0 - buttonSizeFactor2 * 1.0);
                r.Bottom = (int)(drawBoundsHeight - 40.0 - buttonSizeFactor2 * 0.0);
                return r;
            }
            case InitBuy:
            case InitRanking: {
                TinyRect r = new TinyRect();
                r.Left   = (int)(drawBoundsWidth - 20.0 - buttonSizeFactor2 * 0.75);
                r.Right  = (int)(drawBoundsWidth - 20.0 - buttonSizeFactor2 * 0.25);
                r.Top    = (int)(drawBoundsHeight - 20.0 - buttonSizeFactor2 * 2.1);
                r.Bottom = (int)(drawBoundsHeight - 20.0 - buttonSizeFactor2 * 1.6);
                return r;
            }
            case PauseMenu: {
                TinyRect r = new TinyRect();
                r.Left   = (int)((double)pixmapOrigin.X + buttonSizeFactor2 * -0.21);
                r.Right  = (int)((double)pixmapOrigin.X + buttonSizeFactor2 * 0.79);
                r.Top    = (int)((double)pixmapOrigin.Y + buttonSizeFactor2 * 2.2);
                r.Bottom = (int)((double)pixmapOrigin.Y + buttonSizeFactor2 * 3.2);
                return r;
            }
            case PauseBack: {
                TinyRect r = new TinyRect();
                r.Left   = (int)((double)pixmapOrigin.X + buttonSizeFactor2 * 0.79);
                r.Right  = (int)((double)pixmapOrigin.X + buttonSizeFactor2 * 1.79);
                r.Top    = (int)((double)pixmapOrigin.Y + buttonSizeFactor2 * 2.2);
                r.Bottom = (int)((double)pixmapOrigin.Y + buttonSizeFactor2 * 3.2);
                return r;
            }
            case PauseSetup: {
                TinyRect r = new TinyRect();
                r.Left   = (int)((double)pixmapOrigin.X + buttonSizeFactor2 * 1.79);
                r.Right  = (int)((double)pixmapOrigin.X + buttonSizeFactor2 * 2.79);
                r.Top    = (int)((double)pixmapOrigin.Y + buttonSizeFactor2 * 2.2);
                r.Bottom = (int)((double)pixmapOrigin.Y + buttonSizeFactor2 * 3.2);
                return r;
            }
            case PauseRestart: {
                TinyRect r = new TinyRect();
                r.Left   = (int)((double)pixmapOrigin.X + buttonSizeFactor2 * 2.79);
                r.Right  = (int)((double)pixmapOrigin.X + buttonSizeFactor2 * 3.79);
                r.Top    = (int)((double)pixmapOrigin.Y + buttonSizeFactor2 * 2.2);
                r.Bottom = (int)((double)pixmapOrigin.Y + buttonSizeFactor2 * 3.2);
                return r;
            }
            case PauseContinue: {
                TinyRect r = new TinyRect();
                r.Left   = (int)((double)pixmapOrigin.X + buttonSizeFactor2 * 3.79);
                r.Right  = (int)((double)pixmapOrigin.X + buttonSizeFactor2 * 4.79);
                r.Top    = (int)((double)pixmapOrigin.Y + buttonSizeFactor2 * 2.2);
                r.Bottom = (int)((double)pixmapOrigin.Y + buttonSizeFactor2 * 3.2);
                return r;
            }
            case ResumeMenu: {
                TinyRect r = new TinyRect();
                r.Left   = (int)((double)pixmapOrigin.X + buttonSizeFactor2 * 1.29);
                r.Right  = (int)((double)pixmapOrigin.X + buttonSizeFactor2 * 2.29);
                r.Top    = (int)((double)pixmapOrigin.Y + buttonSizeFactor2 * 2.2);
                r.Bottom = (int)((double)pixmapOrigin.Y + buttonSizeFactor2 * 3.2);
                return r;
            }
            case ResumeContinue: {
                TinyRect r = new TinyRect();
                r.Left   = (int)((double)pixmapOrigin.X + buttonSizeFactor2 * 2.29);
                r.Right  = (int)((double)pixmapOrigin.X + buttonSizeFactor2 * 3.29);
                r.Top    = (int)((double)pixmapOrigin.Y + buttonSizeFactor2 * 2.2);
                r.Bottom = (int)((double)pixmapOrigin.Y + buttonSizeFactor2 * 3.2);
                return r;
            }
            case WinLostReturn: {
                TinyRect r = new TinyRect();
                r.Left   = (int)((double)pixmapOrigin.X + drawBoundsWidth - buttonSizeFactor1 * 2.2);
                r.Right  = (int)((double)pixmapOrigin.X + drawBoundsWidth - buttonSizeFactor1 * 1.2);
                r.Top    = (int)((double)pixmapOrigin.Y + buttonSizeFactor1 * 0.2);
                r.Bottom = (int)((double)pixmapOrigin.Y + buttonSizeFactor1 * 1.2);
                return r;
            }
            case TrialBuy: {
                TinyRect r = new TinyRect();
                r.Left   = (int)((double)pixmapOrigin.X + buttonSizeFactor2 * 2.5);
                r.Right  = (int)((double)pixmapOrigin.X + buttonSizeFactor2 * 3.5);
                r.Top    = (int)((double)pixmapOrigin.Y + buttonSizeFactor2 * 2.1);
                r.Bottom = (int)((double)pixmapOrigin.Y + buttonSizeFactor2 * 3.1);
                return r;
            }
            case TrialCancel: {
                TinyRect r = new TinyRect();
                r.Left   = (int)((double)pixmapOrigin.X + buttonSizeFactor2 * 3.5);
                r.Right  = (int)((double)pixmapOrigin.X + buttonSizeFactor2 * 4.5);
                r.Top    = (int)((double)pixmapOrigin.Y + buttonSizeFactor2 * 2.1);
                r.Bottom = (int)((double)pixmapOrigin.Y + buttonSizeFactor2 * 3.1);
                return r;
            }
            case RankingContinue: {
                TinyRect r = new TinyRect();
                r.Left   = (int)((double)pixmapOrigin.X + buttonSizeFactor2 * 3.5);
                r.Right  = (int)((double)pixmapOrigin.X + buttonSizeFactor2 * 4.5);
                r.Top    = (int)((double)pixmapOrigin.Y + buttonSizeFactor2 * 2.1);
                r.Bottom = (int)((double)pixmapOrigin.Y + buttonSizeFactor2 * 3.1);
                return r;
            }
            case SetupSounds: {
                TinyRect r = new TinyRect();
                r.Left   = leftXForButtonsInLeftColumn;
                r.Right  = rightXForButtonsInLeftColumn;
                r.Top    = (int)(drawBoundsHeight - 20.0 - buttonSizeFactor2 * 2.0);
                r.Bottom = (int)(drawBoundsHeight - 20.0 - buttonSizeFactor2 * 1.5);
                return r;
            }
            case SetupJump: {
                TinyRect r = new TinyRect();
                r.Left   = leftXForButtonsInLeftColumn;
                r.Right  = rightXForButtonsInLeftColumn;
                r.Top    = (int)(drawBoundsHeight - 20.0 - buttonSizeFactor2 * 1.5);
                r.Bottom = (int)(drawBoundsHeight - 20.0 - buttonSizeFactor2 * 1.0);
                return r;
            }
            case SetupZoom: {
                TinyRect r = new TinyRect();
                r.Left   = leftXForButtonsInLeftColumn;
                r.Right  = rightXForButtonsInLeftColumn;
                r.Top    = (int)(drawBoundsHeight - 20.0 - buttonSizeFactor2 * 1.0);
                r.Bottom = (int)(drawBoundsHeight - 20.0 - buttonSizeFactor2 * 0.5);
                return r;
            }
            case SetupAccel: {
                TinyRect r = new TinyRect();
                r.Left   = leftXForButtonsInLeftColumn;
                r.Right  = rightXForButtonsInLeftColumn;
                r.Top    = (int)(drawBoundsHeight - 20.0 - buttonSizeFactor2 * 0.5);
                r.Bottom = (int)(drawBoundsHeight - 20.0 - buttonSizeFactor2 * 0.0);
                return r;
            }
            case SetupReset: {
                TinyRect r = new TinyRect();
                r.Left   = (int)(450.0 + buttonSizeFactor2 * 0.0);
                r.Right  = (int)(450.0 + buttonSizeFactor2 * 0.5);
                r.Top    = (int)(drawBoundsHeight - 20.0 - buttonSizeFactor2 * 2.0);
                r.Bottom = (int)(drawBoundsHeight - 20.0 - buttonSizeFactor2 * 1.5);
                return r;
            }
            case SetupReturn: {
                TinyRect r = new TinyRect();
                r.Left   = (int)(drawBoundsWidth - 20.0 - buttonSizeFactor2 * 0.8);
                r.Right  = (int)(drawBoundsWidth - 20.0 - buttonSizeFactor2 * 0.0);
                r.Top    = (int)(drawBoundsHeight - 20.0 - buttonSizeFactor2 * 0.8);
                r.Bottom = (int)(drawBoundsHeight - 20.0 - buttonSizeFactor2 * 0.0);
                return r;
            }
            case PlayPause: {
                TinyRect r = new TinyRect();
                r.Left   = (int)(drawBoundsWidth - buttonSizeFactor1 * 0.7);
                r.Right  = (int)(drawBoundsWidth - buttonSizeFactor1 * 0.2);
                r.Top    = (int)(buttonSizeFactor1 * 0.2);
                r.Bottom = (int)(buttonSizeFactor1 * 0.7);
                return r;
            }
            case PlayAction: {
                if (gameData.JumpRight()) {
                    TinyRect r = new TinyRect();
                    r.Left   = (int)(drawBoundsWidth - buttonSizeFactor1 * 1.2);
                    r.Right  = (int)(drawBoundsWidth - buttonSizeFactor1 * 0.2);
                    r.Top    = (int)(drawBoundsHeight - buttonSizeFactor1 * 2.6);
                    r.Bottom = (int)(drawBoundsHeight - buttonSizeFactor1 * 1.6);
                    return r;
                } else {
                    TinyRect r = new TinyRect();
                    r.Left   = (int)(buttonSizeFactor1 * 0.2);
                    r.Right  = (int)(buttonSizeFactor1 * 1.2);
                    r.Top    = (int)(drawBoundsHeight - buttonSizeFactor1 * 2.6);
                    r.Bottom = (int)(drawBoundsHeight - buttonSizeFactor1 * 1.6);
                    return r;
                }
            }
            case PlayJump: {
                if (gameData.JumpRight()) {
                    TinyRect r = new TinyRect();
                    r.Left   = (int)(drawBoundsWidth - buttonSizeFactor1 * 1.2);
                    r.Right  = (int)(drawBoundsWidth - buttonSizeFactor1 * 0.2);
                    r.Top    = (int)(drawBoundsHeight - buttonSizeFactor1 * 1.2);
                    r.Bottom = (int)(drawBoundsHeight - buttonSizeFactor1 * 0.2);
                    return r;
                } else {
                    TinyRect r = new TinyRect();
                    r.Left   = (int)(buttonSizeFactor1 * 0.2);
                    r.Right  = (int)(buttonSizeFactor1 * 1.2);
                    r.Top    = (int)(drawBoundsHeight - buttonSizeFactor1 * 1.2);
                    r.Bottom = (int)(drawBoundsHeight - buttonSizeFactor1 * 0.2);
                    return r;
                }
            }
            case PlayDown: {
                if (gameData.JumpRight()) {
                    TinyRect r = new TinyRect();
                    r.Left   = (int)(buttonSizeFactor1 * 0.2);
                    r.Right  = (int)(buttonSizeFactor1 * 1.2);
                    r.Top    = (int)(drawBoundsHeight - buttonSizeFactor1 * 1.2);
                    r.Bottom = (int)(drawBoundsHeight - buttonSizeFactor1 * 0.2);
                    return r;
                } else {
                    TinyRect r = new TinyRect();
                    r.Left   = (int)(drawBoundsWidth - buttonSizeFactor1 * 1.2);
                    r.Right  = (int)(drawBoundsWidth - buttonSizeFactor1 * 0.2);
                    r.Top    = (int)(drawBoundsHeight - buttonSizeFactor1 * 1.2);
                    r.Bottom = (int)(drawBoundsHeight - buttonSizeFactor1 * 0.2);
                    return r;
                }
            }
            case Cheat11: {
                TinyRect r = new TinyRect();
                r.Left   = (int)(cheatButtonSizeFactor * 0.0);
                r.Right  = (int)(cheatButtonSizeFactor * 1.0);
                r.Top    = (int)(cheatButtonSizeFactor * 0.0);
                r.Bottom = (int)(cheatButtonSizeFactor * 1.0);
                return r;
            }
            case Cheat12: {
                TinyRect r = new TinyRect();
                r.Left   = (int)(cheatButtonSizeFactor * 0.0);
                r.Right  = (int)(cheatButtonSizeFactor * 1.0);
                r.Top    = (int)(cheatButtonSizeFactor * 1.0);
                r.Bottom = (int)(cheatButtonSizeFactor * 2.0);
                return r;
            }
            case Cheat21: {
                TinyRect r = new TinyRect();
                r.Left   = (int)(cheatButtonSizeFactor * 1.0);
                r.Right  = (int)(cheatButtonSizeFactor * 2.0);
                r.Top    = (int)(cheatButtonSizeFactor * 0.0);
                r.Bottom = (int)(cheatButtonSizeFactor * 1.0);
                return r;
            }
            case Cheat22: {
                TinyRect r = new TinyRect();
                r.Left   = (int)(cheatButtonSizeFactor * 1.0);
                r.Right  = (int)(cheatButtonSizeFactor * 2.0);
                r.Top    = (int)(cheatButtonSizeFactor * 1.0);
                r.Bottom = (int)(cheatButtonSizeFactor * 2.0);
                return r;
            }
            case Cheat31: {
                TinyRect r = new TinyRect();
                r.Left   = (int)(cheatButtonSizeFactor * 2.0);
                r.Right  = (int)(cheatButtonSizeFactor * 3.0);
                r.Top    = (int)(cheatButtonSizeFactor * 0.0);
                r.Bottom = (int)(cheatButtonSizeFactor * 1.0);
                return r;
            }
            case Cheat32: {
                TinyRect r = new TinyRect();
                r.Left   = (int)(cheatButtonSizeFactor * 2.0);
                r.Right  = (int)(cheatButtonSizeFactor * 3.0);
                r.Top    = (int)(cheatButtonSizeFactor * 1.0);
                r.Bottom = (int)(cheatButtonSizeFactor * 2.0);
                return r;
            }
            default:
                return new TinyRect();
        }
    }

    // -----------------------------------------------------------------------
    // Private helpers
    // -----------------------------------------------------------------------

    /**
     * Returns the list of button glyphs that are relevant for the current game
     * phase (replaces the C# iterator / {@code yield return} property).
     */
    private List<Def.ButtonGlyph> GetButtonGlyphs() {
        List<Def.ButtonGlyph> glyphs = new ArrayList<>();
        switch (phase) {
            case Init:
                glyphs.add(Def.ButtonGlyph.InitGamerA);
                glyphs.add(Def.ButtonGlyph.InitGamerB);
                glyphs.add(Def.ButtonGlyph.InitGamerC);
                glyphs.add(Def.ButtonGlyph.InitSetup);
                glyphs.add(Def.ButtonGlyph.InitPlay);
                if (game1.IsTrialMode())   glyphs.add(Def.ButtonGlyph.InitBuy);
                if (game1.IsRankingMode()) glyphs.add(Def.ButtonGlyph.InitRanking);
                break;
            case Play:
                glyphs.add(Def.ButtonGlyph.PlayPause);
                glyphs.add(Def.ButtonGlyph.PlayAction);
                glyphs.add(Def.ButtonGlyph.PlayJump);
                if (accelStarted) glyphs.add(Def.ButtonGlyph.PlayDown);
                glyphs.add(Def.ButtonGlyph.Cheat11);
                glyphs.add(Def.ButtonGlyph.Cheat12);
                glyphs.add(Def.ButtonGlyph.Cheat21);
                glyphs.add(Def.ButtonGlyph.Cheat22);
                glyphs.add(Def.ButtonGlyph.Cheat31);
                glyphs.add(Def.ButtonGlyph.Cheat32);
                break;
            case Pause:
                glyphs.add(Def.ButtonGlyph.PauseMenu);
                if (mission != 1) glyphs.add(Def.ButtonGlyph.PauseBack);
                glyphs.add(Def.ButtonGlyph.PauseSetup);
                if (mission != 1 && mission % 10 != 0) glyphs.add(Def.ButtonGlyph.PauseRestart);
                glyphs.add(Def.ButtonGlyph.PauseContinue);
                break;
            case Resume:
                glyphs.add(Def.ButtonGlyph.ResumeMenu);
                glyphs.add(Def.ButtonGlyph.ResumeContinue);
                break;
            case Lost:
            case Win:
                glyphs.add(Def.ButtonGlyph.WinLostReturn);
                break;
            case Trial:
                glyphs.add(Def.ButtonGlyph.TrialBuy);
                glyphs.add(Def.ButtonGlyph.TrialCancel);
                break;
            case MainSetup:
                glyphs.add(Def.ButtonGlyph.SetupSounds);
                glyphs.add(Def.ButtonGlyph.SetupJump);
                glyphs.add(Def.ButtonGlyph.SetupZoom);
                glyphs.add(Def.ButtonGlyph.SetupAccel);
                glyphs.add(Def.ButtonGlyph.SetupReset);
                glyphs.add(Def.ButtonGlyph.SetupReturn);
                break;
            case PlaySetup:
                glyphs.add(Def.ButtonGlyph.SetupSounds);
                glyphs.add(Def.ButtonGlyph.SetupJump);
                glyphs.add(Def.ButtonGlyph.SetupZoom);
                glyphs.add(Def.ButtonGlyph.SetupAccel);
                glyphs.add(Def.ButtonGlyph.SetupReturn);
                break;
            case Ranking:
                glyphs.add(Def.ButtonGlyph.RankingContinue);
                break;
            default:
                break;
        }
        if (showCheatMenu) {
            glyphs.add(Def.ButtonGlyph.Cheat1);
            glyphs.add(Def.ButtonGlyph.Cheat2);
            glyphs.add(Def.ButtonGlyph.Cheat3);
            glyphs.add(Def.ButtonGlyph.Cheat4);
            glyphs.add(Def.ButtonGlyph.Cheat5);
            glyphs.add(Def.ButtonGlyph.Cheat6);
            glyphs.add(Def.ButtonGlyph.Cheat7);
            glyphs.add(Def.ButtonGlyph.Cheat8);
            glyphs.add(Def.ButtonGlyph.Cheat9);
        }
        return glyphs;
    }

    /** Returns the screen-space center of the directional pad. */
    private TinyPoint GetPadCenter() {
        TinyRect drawBounds = pixmap.DrawBounds();
        int x = gameData.JumpRight() ? 100 : (drawBounds.Right - drawBounds.Left) - 100;
        return new TinyPoint(x, (drawBounds.Bottom - drawBounds.Top) - 100);
    }

    /**
     * Detects which button (if any) the given point falls inside.
     * Iterates the active glyph list in reverse so later-registered glyphs
     * (rendered on top) win the hit-test.
     */
    private Def.ButtonGlyph ButtonDetect(TinyPoint touchOrClick) {
        List<Def.ButtonGlyph> glyphs = GetButtonGlyphs();
        // Reverse iteration replicates C# .Reverse() on the IEnumerable
        for (int i = glyphs.size() - 1; i >= 0; i--) {
            Def.ButtonGlyph bg   = glyphs.get(i);
            TinyRect buttonRect  = GetButtonRect(bg);
            if (bg == Def.ButtonGlyph.PlayJump
                    || bg == Def.ButtonGlyph.PlayAction
                    || bg == Def.ButtonGlyph.PlayDown
                    || bg == Def.ButtonGlyph.PlayPause) {
                buttonRect = Misc.Inflate(buttonRect, 20);
            }
            if (Misc.IsInside(buttonRect, touchOrClick)) {
                return bg;
            }
        }
        return Def.ButtonGlyph.None;
    }

    /** Returns the bounding rectangle for the directional pad disc. */
    private TinyRect GetPadBounds(TinyPoint center, int radius) {
        return new TinyRect(
                center.X - radius, center.X + radius,
                center.Y - radius, center.Y + radius);
    }

    // -----------------------------------------------------------------------
    // Accelerometer stub start / stop
    // -----------------------------------------------------------------------

    /**
     * Attempts to start the accelerometer sensor.
     * On this (LibGDX desktop) platform the stub always succeeds but reports
     * zero so the accel-based movement path remains dormant.
     */
    private void StartAccel() {
        try {
            accelSensor.Start();
            accelStarted = true;
        } catch (Exception e) {
            accelStarted = false;
        }
    }

    /** Stops the accelerometer sensor if it was started. */
    private void StopAccel() {
        if (accelStarted) {
            try {
                accelSensor.Stop();
            } catch (Exception ignored) {
            }
            accelStarted = false;
        }
    }
}
