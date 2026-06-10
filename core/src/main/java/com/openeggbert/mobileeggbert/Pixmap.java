package com.openeggbert.mobileeggbert;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import Microsoft.Xna.Framework.Color;
import Microsoft.Xna.Framework.GraphicsDeviceManager;
import Microsoft.Xna.Framework.Rectangle;
import Microsoft.Xna.Framework.Vector2;
import Microsoft.Xna.Framework.Graphics.*;

public class Pixmap {
    private final Game1 game1;
    private final GraphicsDeviceManager graphics;

    private double zoom;
    private double originX;
    private double originY;
    private double hotSpotZoom;
    private double hotSpotX;
    private double hotSpotY;

    private SpriteBatch spriteBatch;
    private Texture2D bitmapText;
    private Texture2D bitmapButton;
    private Texture2D bitmapJauge;
    private Texture2D bitmapBlupi;
    private Texture2D bitmapBlupi1;
    private Texture2D bitmapObject;
    private Texture2D bitmapElement;
    private Texture2D bitmapExplo;
    private Texture2D bitmapPad;
    private Texture2D bitmapSpeedyBlupi;
    private Texture2D bitmapBlupiYoupie;
    private Texture2D bitmapGear;
    private Texture2D bitmapBackground;

    private final Vector2 origin = new Vector2(0f, 0f);
    private SpriteEffects effect = SpriteEffects.None;

    public TinyRect DrawBounds() {
        TinyRect result = new TinyRect();
        double screenWidth = graphics.GraphicsDevice().Viewport().Width();
        double screenHeight = graphics.GraphicsDevice().Viewport().Height();
        if (Env.PLATFORM == EnvClasses.Platform.Android && screenHeight > 480) {
            screenWidth = screenHeight * (640.0 / 480.0);
        }
        if (screenWidth != 0.0 && screenHeight != 0.0) {
            double drawWidth, drawHeight;
            if (screenWidth / screenHeight < 1.3333333333333333) {
                drawWidth = 640.0;
                drawHeight = 640.0 * (screenHeight / screenWidth);
            } else {
                drawWidth = 480.0 * (screenWidth / screenHeight);
                drawHeight = 480.0;
            }
            result.Left = 0;
            result.Right = (int) drawWidth;
            result.Top = 0;
            result.Bottom = (int) drawHeight;
        }
        return result;
    }

    public TinyPoint Origin() {
        return new TinyPoint((int) originX, (int) originY);
    }

    public Pixmap(Game1 game1, GraphicsDeviceManager graphics) {
        this.game1 = game1;
        this.graphics = graphics;
    }

    public TinyPoint HotSpotToHud(TinyPoint pos) {
        TinyPoint result = new TinyPoint();
        result.X = (int)((double)(pos.X - (int)hotSpotX) / hotSpotZoom) + (int)hotSpotX - (int)originX;
        result.Y = (int)((double)(pos.Y - (int)hotSpotY) / hotSpotZoom) + (int)hotSpotY - (int)originY;
        return result;
    }

    public void SetHotSpot(double zoom, double x, double y) {
        hotSpotZoom = zoom; hotSpotX = x; hotSpotY = y;
    }

    public void DrawInputButton(TinyRect rect, Def.ButtonGlyph glyph, boolean pressed, boolean selected) {
        switch (glyph) {
            case InitGamerA:   DrawIcon(14, selected ? 16 : 4, rect, pressed ? 0.8 : 1.0, false); break;
            case InitGamerB:   DrawIcon(14, selected ? 17 : 5, rect, pressed ? 0.8 : 1.0, false); break;
            case InitGamerC:   DrawIcon(14, selected ? 18 : 6, rect, pressed ? 0.8 : 1.0, false); break;
            case InitSetup:
            case PauseSetup:   DrawIcon(14, 19, rect, pressed ? 0.8 : 1.0, false); break;
            case InitPlay:     DrawIcon(14, 7, rect, pressed ? 0.8 : 1.0, false); break;
            case PauseMenu:
            case ResumeMenu:   DrawIcon(14, 11, rect, pressed ? 0.8 : 1.0, false); break;
            case PauseBack:    DrawIcon(14, 8, rect, pressed ? 0.8 : 1.0, false); break;
            case PauseRestart: DrawIcon(14, 9, rect, pressed ? 0.8 : 1.0, false); break;
            case PauseContinue:
            case ResumeContinue: DrawIcon(14, 10, rect, pressed ? 0.8 : 1.0, false); break;
            case WinLostReturn: DrawIcon(14, 3, rect, pressed ? 0.8 : 1.0, false); break;
            case InitBuy:
            case TrialBuy:     DrawIcon(14, 22, rect, pressed ? 0.8 : 1.0, false); break;
            case InitRanking:  DrawIcon(14, 12, rect, pressed ? 0.8 : 1.0, false); break;
            case TrialCancel:
            case RankingContinue: DrawIcon(14, 8, rect, pressed ? 0.8 : 1.0, false); break;
            case SetupSounds:
            case SetupJump:
            case SetupZoom:
            case SetupAccel:   DrawIcon(14, selected ? 13 : 21, rect, pressed ? 0.8 : 1.0, false); break;
            case SetupReset:   DrawIcon(14, 20, rect, pressed ? 0.8 : 1.0, false); break;
            case SetupReturn:  DrawIcon(14, 8, rect, pressed ? 0.8 : 1.0, false); break;
            case PlayJump:     DrawIcon(14, 2, rect, pressed ? 0.6 : 1.0, false); break;
            case PlayAction:   DrawIcon(14, 12, rect, pressed ? 0.6 : 1.0, false); break;
            case PlayDown:     DrawIcon(14, 23, rect, pressed ? 0.6 : 1.0, false); break;
            case PlayPause:    DrawIcon(14, 3, rect, pressed ? 0.6 : 1.0, false); break;
            case Cheat1: case Cheat2: case Cheat3: case Cheat4: case Cheat5:
            case Cheat6: case Cheat7: case Cheat8: case Cheat9: {
                DrawIcon(14, 0, rect, pressed ? 0.6 : 1.0, false);
                TinyPoint pos = new TinyPoint(
                    rect.Left + rect.Width() / 2 - (int)originX,
                    rect.Top + 28
                );
                Text.DrawTextCenter(this, pos, Decor.getCheatTinyText(glyph), 1.0);
                break;
            }
            default: break;
        }
    }

    public void LoadContent() {
        spriteBatch = new SpriteBatch();
        bitmapText     = game1.Content.Load(Texture2D.class, "icons/text");
        bitmapButton   = game1.Content.Load(Texture2D.class, "icons/button");
        bitmapJauge    = game1.Content.Load(Texture2D.class, "icons/jauge");
        bitmapBlupi    = game1.Content.Load(Texture2D.class, "icons/blupi");
        bitmapBlupi1   = game1.Content.Load(Texture2D.class, "icons/blupi1");
        bitmapObject   = game1.Content.Load(Texture2D.class, "icons/object-m");
        bitmapElement  = game1.Content.Load(Texture2D.class, "icons/element");
        bitmapExplo    = game1.Content.Load(Texture2D.class, "icons/explo");
        bitmapPad      = game1.Content.Load(Texture2D.class, "icons/pad");
        bitmapSpeedyBlupi  = game1.Content.Load(Texture2D.class, "backgrounds/speedyblupi");
        bitmapBlupiYoupie  = game1.Content.Load(Texture2D.class, "backgrounds/blupiyoupie");
        bitmapGear     = game1.Content.Load(Texture2D.class, "backgrounds/gear");
        UpdateGeometry();
    }

    private void UpdateGeometry() {
        double screenWidth = graphics.GraphicsDevice().Viewport().Width();
        double screenHeight = graphics.GraphicsDevice().Viewport().Height();
        if (Env.PLATFORM == EnvClasses.Platform.Android && screenHeight > 480) {
            screenWidth = screenHeight * (640.0 / 480.0);
        }
        double widthScale = screenWidth / 640.0;
        double heightScale = screenHeight / 480.0;
        zoom = Math.min(widthScale, heightScale);
        originX = (screenWidth - 640.0 * zoom) / 2.0;
        originY = (screenHeight - 480.0 * zoom) / 2.0;
    }

    public void BackgroundCache(String name) {
        bitmapBackground = game1.Content.Load(Texture2D.class, "backgrounds/" + name);
    }

    public boolean Start() {
        graphics.GraphicsDevice().clear(Color.CornflowerBlue);
        return true;
    }

    public boolean Finish() { return true; }

    public void DrawBackground() {
        double screenWidth = graphics.GraphicsDevice().Viewport().Width();
        double screenHeight = graphics.GraphicsDevice().Viewport().Height();
        if (Env.PLATFORM == EnvClasses.Platform.Android && screenHeight > 480) {
            screenWidth = screenHeight * (640.0 / 480.0);
        }
        Texture2D bitmap = GetBitmap(3);
        Rectangle srcRectangle = GetSrcRectangle(bitmap, 10, 10, 10, 10, 0, 0);
        Rectangle destRectangle = new Rectangle(0, 0, (int) screenWidth, (int) screenHeight);
        spriteBatch.Begin(SpriteSortMode.BackToFront, BlendState.AlphaBlend);
        spriteBatch.Draw(bitmap, destRectangle, srcRectangle, Color.White);
        spriteBatch.End();

        TinyPoint dest = new TinyPoint((int) originX, (int) originY);
        TinyRect rect = new TinyRect(0, 640, 0, 480);
        DrawPart(3, dest, rect);
    }

    public void DrawChar(int rank, TinyPoint pos, double size) {
        pos = pos.Copy();
        pos.X = (int)(pos.X + originX);
        pos.Y = (int)(pos.Y + originY);
        TinyRect rect = new TinyRect(
            pos.X, pos.X + (int)(32.0 * size),
            pos.Y, pos.Y + (int)(32.0 * size)
        );
        DrawIcon(6, rank, rect, 1.0, false);
    }

    public void HudIcon(int channel, int rank, TinyPoint pos) {
        pos = pos.Copy();
        pos.X = (int)(pos.X + originX);
        pos.Y = (int)(pos.Y + originY);
        TinyRect rect = new TinyRect(pos);
        DrawIcon(channel, rank, rect, 1.0, false);
    }

    public void QuickIcon(int channel, int rank, TinyPoint pos) {
        DrawIcon(channel, rank, new TinyRect(pos), 1.0, true);
    }

    public void QuickIcon(int channel, int rank, TinyPoint pos, double opacity, double rotation) {
        DrawIcon(channel, rank, new TinyRect(pos), opacity, rotation, true);
    }

    public boolean DrawPart(int channel, TinyPoint dest, TinyRect rect) {
        return DrawPart(channel, dest, rect, 1.0);
    }

    public boolean DrawPart(int channel, TinyPoint dest, TinyRect rect, double zoomFactor) {
        Texture2D bitmap = GetBitmap(channel);
        if (bitmap == null) return false;
        TinyPoint d = dest.Copy();
        if (channel == 5) {
            d.X = (int)(d.X + originX);
            d.Y = (int)(d.Y + originY);
        }
        Rectangle src = new Rectangle(rect.Left, rect.Top, rect.Width(), rect.Height());
        Rectangle dst = new Rectangle(d.X, d.Y,
            (int)(rect.Width() * zoomFactor),
            (int)(rect.Height() * zoomFactor));
        spriteBatch.Begin(SpriteSortMode.BackToFront, BlendState.AlphaBlend);
        spriteBatch.Draw(bitmap, dst, src, Color.White);
        spriteBatch.End();
        return true;
    }

    public void DrawIcon(int channel, int icon, TinyRect rect, double opacity, boolean useHotSpot) {
        DrawIcon(channel, icon, rect, opacity, 0.0, useHotSpot);
    }

    public void DrawIcon(int channel, int icon, TinyRect rect, double opacity, double rotationDeg, boolean useHotSpot) {
        if (icon == -1) return;
        if (Config.TOUCH_BUTTONS_SHOWN_ONLY_IF_TOUCHSCREEN_IS_AVAILABLE && channel == 14) {
            boolean connected = Microsoft.Xna.Framework.Input.Touch.TouchPanel.GetCapabilities().IsConnected();
            if (!connected) {
                int[] padIcons = {0, 1, 2, 3, 30, 12, 23};
                for (int pIcon : padIcons) {
                    if (pIcon == icon) {
                        if (icon == 1 && rect.Left > 100) continue;
                        return;
                    }
                }
            }
        }
        Texture2D bitmap = GetBitmap(channel);
        if (bitmap == null) return;

        int bitmapGridX, bitmapGridY, iconWidth, iconHeight, gap;
        switch (channel) {
            case 2: case 11: case 12: case 13:
                bitmapGridX = bitmapGridY = iconWidth = iconHeight = 60; gap = 0; break;
            case 1:
                bitmapGridX = bitmapGridY = iconWidth = iconHeight = 64; gap = 1; break;
            case 10:
                bitmapGridX = bitmapGridY = iconWidth = iconHeight = 60; gap = 0; break;
            case 9:
                bitmapGridX = bitmapGridY = 144;
                iconHeight = Tables.table_explo_size[icon];
                iconWidth = Math.max(iconHeight, 128); gap = 0; break;
            case 6:
                bitmapGridX = bitmapGridY = iconWidth = iconHeight = 32; gap = 0; break;
            case 4:
                bitmapGridX = bitmapGridY = iconWidth = iconHeight = 40; gap = 0; break;
            case 14:
                bitmapGridX = bitmapGridY = iconWidth = iconHeight = 140; gap = 0; break;
            case 15:
                bitmapGridX = iconWidth = 640; bitmapGridY = iconHeight = 160; gap = 0; break;
            case 16:
                bitmapGridX = iconWidth = 410; bitmapGridY = iconHeight = 380; gap = 0; break;
            case 17:
                bitmapGridX = bitmapGridY = iconWidth = iconHeight = 226; gap = 0; break;
            default:
                return;
        }

        Rectangle src = GetSrcRectangle(bitmap, bitmapGridX, bitmapGridY, iconWidth, iconHeight, gap, icon);
        Rectangle dst = GetDstRectangle(rect, iconWidth, iconHeight, useHotSpot);
        float rotationRad = 0f;
        if (rotationDeg != 0.0) {
            rotationRad = (float) Misc.DegToRad(rotationDeg);
            dst = Misc.RotateAdjust(dst, rotationRad);
        }
        spriteBatch.Begin(SpriteSortMode.BackToFront, BlendState.AlphaBlend);
        spriteBatch.Draw(bitmap, dst, src,
            Color.fromNonPremultiplied(255, 255, 255, (int)(255.0 * opacity)),
            rotationRad, origin, effect, 0f);
        spriteBatch.End();
    }

    private Rectangle GetSrcRectangle(Texture2D bitmap, int bitmapGridX, int bitmapGridY,
                                       int iconWidth, int iconHeight, int gap, int icon) {
        int width = bitmap.Width();
        int cols = width / bitmapGridX;
        int column = icon % cols;
        int row = icon / cols;
        int gx = bitmapGridX + gap;
        int gy = bitmapGridY + gap;
        return new Rectangle(gap + column * gx, gap + row * gy, iconWidth, iconHeight);
    }

    private Rectangle GetDstRectangle(TinyRect rect, int iconWidth, int iconHeight, boolean useHotSpot) {
        int finalWidth  = (rect.Width()  == 0) ? iconWidth  : rect.Width();
        int finalHeight = (rect.Height() == 0) ? iconHeight : rect.Height();
        int scaledL = (int)(rect.Left * zoom);
        int scaledT = (int)(rect.Top * zoom);
        int scaledR = (int)(scaledL + finalWidth  * zoom);
        int scaledB = (int)(scaledT + finalHeight * zoom);
        if (useHotSpot && hotSpotZoom > 1.0) {
            scaledL -= (int)hotSpotX; scaledT -= (int)hotSpotY;
            scaledR -= (int)hotSpotX; scaledB -= (int)hotSpotY;
            scaledL = (int)(scaledL * hotSpotZoom); scaledT = (int)(scaledT * hotSpotZoom);
            scaledR = (int)(scaledR * hotSpotZoom); scaledB = (int)(scaledB * hotSpotZoom);
            scaledL += (int)hotSpotX; scaledT += (int)hotSpotY;
            scaledR += (int)hotSpotX; scaledB += (int)hotSpotY;
        }
        return new Rectangle(scaledL, scaledT, scaledR - scaledL, scaledB - scaledT);
    }

    private Texture2D GetBitmap(int channel) {
        switch (channel) {
            case 2: return bitmapBlupi;
            case 11: case 12: case 13: return bitmapBlupi1;
            case 1: return bitmapObject;
            case 10: return bitmapElement;
            case 9: return bitmapExplo;
            case 6: return bitmapText;
            case 4: return bitmapButton;
            case 5: return bitmapJauge;
            case 14: return bitmapPad;
            case 15: return bitmapSpeedyBlupi;
            case 16: return bitmapBlupiYoupie;
            case 17: return bitmapGear;
            case 3: return bitmapBackground;
            default: return null;
        }
    }
}
