package com.openeggbert.mobileeggbert;

import com.badlogic.gdx.Gdx;
import Microsoft.Xna.Framework.Game;
import Microsoft.Xna.Framework.GameTime;
import Microsoft.Xna.Framework.GraphicsDeviceManager;
import System.TimeSpan;

public class Game1 extends Game {

    private static final double[] waitTable = {
        0.1, 7.0, 0.2, 20.0, 0.25, 22.0, 0.45, 50.0, 0.6, 53.0,
        0.65, 58.0, 0.68, 60.0, 0.8, 70.0, 0.84, 75.0, 0.9, 84.0,
        0.94, 91.0, 1.0, 100.0
    };

    private static final Def.ButtonGlyph[] cheatGeste = {
        Def.ButtonGlyph.Cheat12,
        Def.ButtonGlyph.Cheat22,
        Def.ButtonGlyph.Cheat32,
        Def.ButtonGlyph.Cheat12,
        Def.ButtonGlyph.Cheat11,
        Def.ButtonGlyph.Cheat21,
        Def.ButtonGlyph.Cheat22,
        Def.ButtonGlyph.Cheat21,
        Def.ButtonGlyph.Cheat31,
        Def.ButtonGlyph.Cheat32
    };

    private final Pixmap pixmap;
    private final Sound sound;
    private final Decor decor;
    private final InputPad inputPad;
    private final GameData gameData;

    private Def.Phase phase;
    private TimeSpan startTime;
    private int missionToStart1;
    private int missionToStart2;
    private int mission;
    private int cheatGesteIndex;
    private int continueMission;
    private Jauge waitJauge;
    private double waitProgress;
    private boolean isTrialMode;
    private boolean simulateTrialMode;
    private boolean playSetup;
    private int phaseTime;
    private Def.Phase fadeOutPhase;
    private int fadeOutMission;

    public boolean IsRankingMode() { return false; }

    public boolean IsTrialMode() {
        return simulateTrialMode || isTrialMode;
    }

    public Game1() {
        if (!Env.INITIALIZED) {
            throw new RuntimeException("Fatal error: Not initialized. Env.init() was not called.");
        }
        missionToStart1 = -1;
        missionToStart2 = -1;
        gameData = new GameData();
        pixmap = new Pixmap(this, graphics);
        sound = new Sound(this, gameData);
        decor = new Decor();
        decor.Create(sound, pixmap, gameData);
        TinyPoint pos = new TinyPoint(196, 426);
        waitJauge = new Jauge();
        waitJauge.Create(pixmap, sound, pos, 3, false);
        waitJauge.SetHide(false);
        waitJauge.SetZoom(2.0);
        phase = Def.Phase.None;
        fadeOutPhase = Def.Phase.None;
        inputPad = new InputPad(this, decor, pixmap, sound, gameData);
        SetPhase(Def.Phase.First);
    }

    @Override
    protected void Initialize() {}

    @Override
    protected void LoadContent() {
        pixmap.BackgroundCache("wait");
    }

    @Override
    protected void Update(GameTime gameTime) {
        phaseTime++;
        if (fadeOutPhase != Def.Phase.None) {
            if (phaseTime >= 20) SetPhase(fadeOutPhase);
            return;
        }
        if (missionToStart2 != -1) {
            SetPhase(Def.Phase.Play, missionToStart2);
            return;
        }
        if (phase == Def.Phase.First) {
            startTime = gameTime.TotalGameTime();
            pixmap.LoadContent();
            sound.LoadContent();
            gameData.Read();
            inputPad.SetPixmapOrigin(pixmap.Origin());
            SetPhase(Def.Phase.Wait);
            return;
        }
        if (phase == Def.Phase.Wait) {
            if (continueMission == 2) {
                continueMission = 0;
                if (decor.CurrentRead()) {
                    SetPhase(Def.Phase.Resume);
                    return;
                }
            }
            long elapsed = gameTime.TotalGameTime().Ticks() - startTime.Ticks();
            waitProgress = (double) elapsed / 50_000_000.0;
            if (waitProgress > 1.0) SetPhase(Def.Phase.Init);
            return;
        }
        inputPad.Update();
        Def.ButtonGlyph buttonPressed = inputPad.ButtonPressed();

        if (buttonPressed.ordinal() >= Def.ButtonGlyph.InitGamerA.ordinal()
                && buttonPressed.ordinal() <= Def.ButtonGlyph.InitGamerC.ordinal()) {
            SetGamer(buttonPressed.ordinal() - Def.ButtonGlyph.InitGamerA.ordinal());
            return;
        }

        switch (buttonPressed) {
            case InitSetup:     SetPhase(Def.Phase.MainSetup); return;
            case PauseSetup:    SetPhase(Def.Phase.PlaySetup); return;
            case SetupSounds:   gameData.SetSounds(!gameData.Sounds()); gameData.Write(); return;
            case SetupJump:     gameData.SetJumpRight(!gameData.JumpRight()); gameData.Write(); return;
            case SetupZoom:     gameData.SetAutoZoom(!gameData.AutoZoom()); gameData.Write(); return;
            case SetupAccel:    gameData.SetAccelActive(!gameData.AccelActive()); gameData.Write(); return;
            case SetupReset:    gameData.Reset(); gameData.Write(); return;
            case SetupReturn:
                if (playSetup) SetPhase(Def.Phase.Play, -1);
                else SetPhase(Def.Phase.Init);
                return;
            case InitPlay:      SetPhase(Def.Phase.Play, 1); return;
            case PlayPause:     SetPhase(Def.Phase.Pause); return;
            case WinLostReturn:
            case PauseMenu:
            case ResumeMenu:    SetPhase(Def.Phase.Init); break;
            default: break;
        }

        switch (buttonPressed) {
            case ResumeContinue:    ContinueMission2(); return;
            case InitBuy:
            case TrialBuy:          SetPhase(Def.Phase.Init); return;
            case InitRanking:       SetPhase(Def.Phase.Ranking); return;
            case TrialCancel:
            case RankingContinue:   SetPhase(Def.Phase.Init); return;
            case PauseBack:         MissionBack(); return;
            case PauseRestart:      SetPhase(Def.Phase.Play, mission); return;
            case PauseContinue:     SetPhase(Def.Phase.Play, -1); return;
            case Cheat11: case Cheat12: case Cheat21: case Cheat22: case Cheat31: case Cheat32:
                if (buttonPressed == cheatGeste[cheatGesteIndex]) {
                    cheatGesteIndex++;
                    if (cheatGesteIndex == cheatGeste.length) {
                        cheatGesteIndex = 0;
                        inputPad.SetShowCheatMenu(true);
                    }
                } else {
                    cheatGesteIndex = 0;
                }
                break;
            default:
                if (buttonPressed != Def.ButtonGlyph.None) cheatGesteIndex = 0;
                break;
        }

        if (buttonPressed.ordinal() >= Def.ButtonGlyph.Cheat1.ordinal()
                && buttonPressed.ordinal() <= Def.ButtonGlyph.Cheat9.ordinal()) {
            CheatAction(buttonPressed);
        }

        if (phase == Def.Phase.Play) {
            decor.SetButtonPressed(buttonPressed);
            decor.MoveStep();
            int result = decor.IsTerminated();
            if (result == -1) {
                MemorizeGamerProgress();
                SetPhase(Def.Phase.Lost);
            } else if (result == -2) {
                MemorizeGamerProgress();
                SetPhase(Def.Phase.Win);
            } else if (result >= 1) {
                MemorizeGamerProgress();
                startMission(result);
            }
        }
    }

    private void MissionBack() {
        int num = mission;
        if (num == 1) { SetPhase(Def.Phase.Init); return; }
        num = (num % 10 == 0) ? 1 : (num / 10 * 10);
        SetPhase(Def.Phase.Play, num);
    }

    private void StartMission(int mission) {
        if (mission > 20 && mission % 10 > 1 && isTrialMode()) {
            SetPhase(Def.Phase.Trial);
            return;
        }
        this.mission = mission;
        if (this.mission != 1) gameData.SetLastWorld(this.mission / 10);
        decor.Read(0, this.mission, false);
        decor.LoadImages();
        decor.SetMission(this.mission);
        decor.SetNbVies(gameData.NbVies());
        decor.InitializeDoors(gameData);
        decor.AdaptDoors(false);
        decor.MainSwitchInitialize(gameData.LastWorld());
        decor.PlayPrepare(false);
        decor.StartSound();
        inputPad.StartMission(this.mission);
    }

    private void ContinueMission2() {
        SetPhase(Def.Phase.Play, -2);
        mission = decor.Mission();
        if (mission != 1) gameData.SetLastWorld(mission / 10);
        decor.LoadImages();
        decor.StartSound();
        inputPad.StartMission(mission);
    }

    private void CheatAction(Def.ButtonGlyph glyph) {
        switch (glyph) {
            case Cheat1: decor.CheatAction(Tables.CheatCodes.OpenDoors); break;
            case Cheat2: decor.CheatAction(Tables.CheatCodes.SuperBlupi); break;
            case Cheat3: decor.CheatAction(Tables.CheatCodes.ShowSecret); break;
            case Cheat4: decor.CheatAction(Tables.CheatCodes.LayEgg); break;
            case Cheat5: gameData.Reset(); break;
            case Cheat6: simulateTrialMode = !simulateTrialMode; break;
            case Cheat7: decor.CheatAction(Tables.CheatCodes.CleanAll); break;
            case Cheat8: decor.CheatAction(Tables.CheatCodes.AllTreasure); break;
            case Cheat9: decor.CheatAction(Tables.CheatCodes.EndGoal); break;
            default: break;
        }
    }

    @Override
    protected void Draw(GameTime gameTime) {
        if (continueMission == 1) continueMission = 2;
        if (phase == Def.Phase.Wait || phase == Def.Phase.Init || phase == Def.Phase.Pause
                || phase == Def.Phase.Resume || phase == Def.Phase.Lost || phase == Def.Phase.Win
                || phase == Def.Phase.MainSetup || phase == Def.Phase.PlaySetup
                || phase == Def.Phase.Trial || phase == Def.Phase.Ranking) {
            pixmap.DrawBackground();
            if (fadeOutPhase == Def.Phase.None && missionToStart1 != -1) {
                missionToStart2 = missionToStart1;
                missionToStart1 = -1;
            } else {
                DrawBackgroundFade();
                if (fadeOutPhase == Def.Phase.None) {
                    DrawButtonsBackground();
                    inputPad.draw();
                    DrawButtonsText();
                }
            }
        } else if (phase == Def.Phase.Play) {
            decor.Build();
            inputPad.draw();
        }
        if (phase == Def.Phase.Wait) DrawWaitProgress();
    }

    private void DrawBackgroundFade() {
        if (phase == Def.Phase.Init) {
            double num = Math.min((double) phaseTime / 20.0, 1.0);
            TinyRect rect;
            double opacity;
            if (fadeOutPhase == Def.Phase.MainSetup) {
                num = (1.0 - num) * (1.0 - num);
                rect = new TinyRect((int)(720.0 - 640.0 * num), (int)(1360.0 - 640.0 * num), 0, 160);
                opacity = num * num;
            } else {
                num = (fadeOutPhase != Def.Phase.None) ? (1.0 - num * 2.0) : (1.0 - (1.0 - num) * (1.0 - num));
                rect = new TinyRect(80, 720, (int)(-160.0 + num * 160.0), (int)(num * 160.0));
                opacity = 1.0;
            }
            pixmap.DrawIcon(15, 0, rect, opacity, false);
        }
        if (phase == Def.Phase.Init) {
            double num = Math.min((double) phaseTime / 20.0, 1.0);
            double opacity;
            if (fadeOutPhase == Def.Phase.MainSetup) {
                opacity = (1.0 - num) * (1.0 - num);
                num = 1.0;
            } else if (fadeOutPhase == Def.Phase.None) {
                num = 0.5 + num / 2.0;
                opacity = Math.min(num * num, 1.0);
            } else {
                opacity = 1.0 - num;
                num = 1.0 + num * 10.0;
            }
            TinyRect rect = new TinyRect(
                (int)(468.0 - 205.0 * num), (int)(468.0 + 205.0 * num),
                (int)(280.0 - 190.0 * num), (int)(280.0 + 190.0 * num));
            pixmap.DrawIcon(16, 0, rect, opacity, 0.0, false);
        }
        if (phase == Def.Phase.Pause || phase == Def.Phase.Resume) {
            if (fadeOutPhase == Def.Phase.Play) {
                double num = Math.min((double) phaseTime / 20.0, 1.0);
                TinyRect rect = new TinyRect(
                    (int)(418.0 - 205.0 * num), (int)(418.0 + 205.0 * num),
                    (int)(190.0 - 190.0 * num), (int)(190.0 + 190.0 * num));
                pixmap.DrawIcon(16, 0, rect, 1.0 - num, 0.0, false);
            } else if (fadeOutPhase == Def.Phase.PlaySetup) {
                double num = Math.min((double) phaseTime / 20.0, 1.0);
                num *= num;
                TinyRect rect = new TinyRect(
                    (int)(213.0 + 800.0 * num), (int)(623.0 + 800.0 * num), 0, 0);
                pixmap.DrawIcon(16, 0, rect, 1.0, 0.0, false);
            } else {
                double num = Math.min((double) phaseTime / 15.0, 1.0);
                if (fadeOutPhase != Def.Phase.None) num = 1.0 - num;
                TinyRect rect = new TinyRect(
                    (int)(418.0 - 205.0 * num), (int)(418.0 + 205.0 * num),
                    (int)(190.0 - 190.0 * num), (int)(190.0 + 190.0 * num));
                double rotation = 0.0;
                if (num < 1.0) rotation = (1.0 - num) * (1.0 - num) * 360.0;
                if (rect.Width() > 0 && rect.Height() > 0) {
                    pixmap.DrawIcon(16, 0, rect, 1.0, rotation, false);
                }
            }
        }
        if (phase == Def.Phase.MainSetup || phase == Def.Phase.PlaySetup) {
            double num = Math.min((double) phaseTime / 20.0, 1.0);
            num = 1.0 - (1.0 - num) * (1.0 - num);
            double num2 = (phaseTime < 20)
                ? (1.0 - (1.0 - (double)phaseTime/20.0) * (1.0 - (double)phaseTime/20.0))
                : (1.0 + ((double)phaseTime - 20.0) / 400.0);
            if (fadeOutPhase != Def.Phase.None) { num = 1.0 - num; num2 = 1.0 - num2; }
            TinyRect rect = new TinyRect(
                (int)(720.0 - 640.0 * num), (int)(1360.0 - 640.0 * num), 0, 160);
            pixmap.DrawIcon(15, 0, rect, num * num, false);
            TinyRect rect2 = new TinyRect(487, 713, 148, 374);
            TinyRect rect3 = new TinyRect(118, 570, 268, 720);
            double opacity = 0.5 - num * 0.4;
            double rotation = -num2 * 100.0 * 2.5;
            pixmap.DrawIcon(17, 0, rect2, opacity, rotation, false);
            pixmap.DrawIcon(17, 0, rect3, opacity, -rotation * 0.5, false);
        }
        if (phase == Def.Phase.Lost) {
            double num = Math.min((double) phaseTime / 100.0, 1.0);
            TinyRect rect = new TinyRect(
                (int)(418.0 - 205.0 * num), (int)(418.0 + 205.0 * num),
                (int)(238.0 - 190.0 * num), (int)(238.0 + 190.0 * num));
            double rotation = (num < 1.0) ? (1.0 - num) * (1.0 - num) * 360.0 * 6.0 : 0.0;
            if (rect.Width() > 0 && rect.Height() > 0) {
                pixmap.DrawIcon(16, 0, rect, 1.0, rotation, false);
            }
        }
        if (phase == Def.Phase.Win) {
            double num = Math.sin((double) phaseTime / 3.0) / 2.0 + 1.0;
            TinyRect rect = new TinyRect(
                (int)(418.0 - 205.0 * num), (int)(418.0 + 205.0 * num),
                (int)(238.0 - 190.0 * num), (int)(238.0 + 190.0 * num));
            pixmap.DrawIcon(16, 0, rect, 1.0, 0.0, false);
        }
    }

    private void DrawButtonsBackground() {
        if (phase == Def.Phase.Init) {
            TinyRect drawBounds = pixmap.DrawBounds();
            int width = drawBounds.Width();
            int height = drawBounds.Height();
            TinyRect rect = new TinyRect(10, 260, height - 325, height - 10);
            pixmap.DrawIcon(14, 15, rect, 0.3, false);
            TinyRect rect2 = new TinyRect(width - 170, width - 10,
                height - ((isTrialMode() || isRankingMode()) ? 325 : 195), height - 10);
            pixmap.DrawIcon(14, 15, rect2, 0.3, false);
        }
    }

    private void DrawButtonsText() {
        if (phase == Def.Phase.Init) {
            DrawButtonGamerText(Def.ButtonGlyph.InitGamerA, 0);
            DrawButtonGamerText(Def.ButtonGlyph.InitGamerB, 1);
            DrawButtonGamerText(Def.ButtonGlyph.InitGamerC, 2);
            DrawTextUnderButton(Def.ButtonGlyph.InitPlay, MyResource.TX_BUTTON_PLAY);
            DrawTextRightButton(Def.ButtonGlyph.InitSetup, MyResource.TX_BUTTON_SETUP);
            if (isTrialMode()) DrawTextUnderButton(Def.ButtonGlyph.InitBuy, MyResource.TX_BUTTON_BUY);
            if (isRankingMode()) DrawTextUnderButton(Def.ButtonGlyph.InitRanking, MyResource.TX_BUTTON_RANKING);
        }
        if (phase == Def.Phase.Pause) {
            DrawTextUnderButton(Def.ButtonGlyph.PauseMenu, MyResource.TX_BUTTON_MENU);
            if (mission != 1) DrawTextUnderButton(Def.ButtonGlyph.PauseBack, MyResource.TX_BUTTON_BACK);
            DrawTextUnderButton(Def.ButtonGlyph.PauseSetup, MyResource.TX_BUTTON_SETUP);
            if (mission != 1 && mission % 10 != 0) DrawTextUnderButton(Def.ButtonGlyph.PauseRestart, MyResource.TX_BUTTON_RESTART);
            DrawTextUnderButton(Def.ButtonGlyph.PauseContinue, MyResource.TX_BUTTON_CONTINUE);
        }
        if (phase == Def.Phase.Resume) {
            DrawTextUnderButton(Def.ButtonGlyph.ResumeMenu, MyResource.TX_BUTTON_MENU);
            DrawTextUnderButton(Def.ButtonGlyph.ResumeContinue, MyResource.TX_BUTTON_CONTINUE);
        }
        if (phase == Def.Phase.MainSetup || phase == Def.Phase.PlaySetup) {
            DrawTextRightButton(Def.ButtonGlyph.SetupSounds, MyResource.TX_BUTTON_SETUP_SOUNDS);
            DrawTextRightButton(Def.ButtonGlyph.SetupJump, MyResource.TX_BUTTON_SETUP_JUMP);
            DrawTextRightButton(Def.ButtonGlyph.SetupZoom, MyResource.TX_BUTTON_SETUP_ZOOM);
            DrawTextRightButton(Def.ButtonGlyph.SetupAccel, MyResource.TX_BUTTON_SETUP_ACCEL);
            if (phase == Def.Phase.MainSetup) {
                String text = String.format(MyResource.loadString(MyResource.TX_BUTTON_SETUP_RESET),
                    String.valueOf((char)(65 + gameData.SelectedGamer())));
                DrawTextRightButton(Def.ButtonGlyph.SetupReset, text);
            }
        }
        if (phase == Def.Phase.Trial) {
            TinyPoint pos = new TinyPoint(360, 50);
            Text.DrawText(pixmap, pos, MyResource.loadString(MyResource.TX_TRIAL1), 0.9);
            pos.Y += 40; Text.DrawText(pixmap, pos, MyResource.loadString(MyResource.TX_TRIAL2), 0.7);
            pos.Y += 25; Text.DrawText(pixmap, pos, MyResource.loadString(MyResource.TX_TRIAL3), 0.7);
            pos.Y += 25; Text.DrawText(pixmap, pos, MyResource.loadString(MyResource.TX_TRIAL4), 0.7);
            pos.Y += 25; Text.DrawText(pixmap, pos, MyResource.loadString(MyResource.TX_TRIAL5), 0.7);
            pos.Y += 25; Text.DrawText(pixmap, pos, MyResource.loadString(MyResource.TX_TRIAL6), 0.7);
            DrawTextUnderButton(Def.ButtonGlyph.TrialBuy, MyResource.TX_BUTTON_BUY);
            DrawTextUnderButton(Def.ButtonGlyph.TrialCancel, MyResource.TX_BUTTON_BACK);
        }
        if (phase == Def.Phase.Ranking) {
            DrawTextUnderButton(Def.ButtonGlyph.RankingContinue, MyResource.TX_BUTTON_BACK);
        }
    }

    private void DrawButtonGamerText(Def.ButtonGlyph glyph, int gamer) {
        TinyRect buttonRect = inputPad.GetButtonRect(glyph);
        int[] nbVies = new int[1], mainDoors = new int[1], secondaryDoors = new int[1];
        gameData.GetGamerInfo(gamer, nbVies, mainDoors, secondaryDoors);
        TinyPoint pos = new TinyPoint(
            buttonRect.Right + 5 - pixmap.Origin().X,
            buttonRect.Top + 3 - pixmap.Origin().Y);
        String text = String.format(MyResource.loadString(MyResource.TX_GAMER_TITLE),
            String.valueOf((char)(65 + gamer)));
        Text.DrawText(pixmap, pos, text, 0.7);
        pos = new TinyPoint(buttonRect.Right + 5 - pixmap.Origin().X, buttonRect.Top + 25 - pixmap.Origin().Y);
        text = String.format(MyResource.loadString(MyResource.TX_GAMER_MDOORS), mainDoors[0]);
        Text.DrawText(pixmap, pos, text, 0.45);
        pos = new TinyPoint(buttonRect.Right + 5 - pixmap.Origin().X, buttonRect.Top + 39 - pixmap.Origin().Y);
        text = String.format(MyResource.loadString(MyResource.TX_GAMER_SDOORS), secondaryDoors[0]);
        Text.DrawText(pixmap, pos, text, 0.45);
        pos = new TinyPoint(buttonRect.Right + 5 - pixmap.Origin().X, buttonRect.Top + 53 - pixmap.Origin().Y);
        text = String.format(MyResource.loadString(MyResource.TX_GAMER_LIFES), nbVies[0]);
        Text.DrawText(pixmap, pos, text, 0.45);
    }

    private void DrawTextRightButton(Def.ButtonGlyph glyph, int res) {
        DrawTextRightButton(glyph, MyResource.loadString(res));
    }

    private void DrawTextRightButton(Def.ButtonGlyph glyph, String text) {
        TinyRect buttonRect = inputPad.GetButtonRect(glyph);
        String[] lines = text.split("\n");
        if (lines.length == 2) {
            TinyPoint pos = new TinyPoint(
                buttonRect.Right + 10 - pixmap.Origin().X,
                (buttonRect.Top + buttonRect.Bottom) / 2 - 20 - pixmap.Origin().Y);
            Text.DrawText(pixmap, pos, lines[0], 0.7);
            pos.Y += 24;
            Text.DrawText(pixmap, pos, lines[1], 0.7);
        } else {
            TinyPoint pos = new TinyPoint(
                buttonRect.Right + 10 - pixmap.Origin().X,
                (buttonRect.Top + buttonRect.Bottom) / 2 - 8 - pixmap.Origin().Y);
            Text.DrawText(pixmap, pos, text, 0.7);
        }
    }

    private void DrawTextUnderButton(Def.ButtonGlyph glyph, int res) {
        TinyRect buttonRect = inputPad.GetButtonRect(glyph);
        TinyPoint pos = new TinyPoint(
            (buttonRect.Left + buttonRect.Right) / 2 - pixmap.Origin().X,
            buttonRect.Bottom + 2 - pixmap.Origin().Y);
        Text.DrawTextCenter(pixmap, pos, MyResource.loadString(res), 0.7);
    }

    private void DrawWaitProgress() {
        if (continueMission != 0) return;
        for (int i = 0; i < waitTable.length / 2; i++) {
            if (waitProgress <= waitTable[i * 2]) {
                waitJauge.SetLevel((int) waitTable[i * 2 + 1]);
                break;
            }
        }
        waitJauge.Draw();
    }

    private void SetGamer(int gamer) {
        gameData.SetSelectedGamer(gamer);
        gameData.Write();
    }

    private void SetPhase(Def.Phase phase) {
        SetPhase(phase, 0);
    }

    private void SetPhase(Def.Phase phase, int mission) {
        if (mission != -2) {
            if (missionToStart2 == -1) {
                if ((this.phase == Def.Phase.Init || this.phase == Def.Phase.MainSetup
                        || this.phase == Def.Phase.PlaySetup || this.phase == Def.Phase.Pause
                        || this.phase == Def.Phase.Resume) && fadeOutPhase == Def.Phase.None) {
                    fadeOutPhase = phase;
                    fadeOutMission = mission;
                    phaseTime = 0;
                    return;
                }
                if (phase == Def.Phase.Play) {
                    fadeOutPhase = Def.Phase.None;
                    if (fadeOutMission != -1) {
                        missionToStart1 = fadeOutMission;
                        return;
                    }
                    mission = fadeOutMission;
                    decor.LoadImages();
                }
            } else {
                mission = missionToStart2;
            }
        }
        this.phase = phase;
        fadeOutPhase = Def.Phase.None;
        inputPad.SetPhase(this.phase);
        playSetup = (this.phase == Def.Phase.PlaySetup);
        isTrialMode = false;
        phaseTime = 0;
        missionToStart2 = -1;
        decor.StopSound();
        switch (this.phase) {
            case Init:     pixmap.BackgroundCache("init"); break;
            case Pause:
            case Resume:   pixmap.BackgroundCache("pause"); break;
            case Lost:     pixmap.BackgroundCache("lost"); break;
            case Win:      pixmap.BackgroundCache("win"); break;
            case MainSetup:
            case PlaySetup: pixmap.BackgroundCache("setup"); break;
            case Trial:    pixmap.BackgroundCache("trial"); break;
            case Ranking:  pixmap.BackgroundCache("pause"); break;
            case Play:     decor.SetDrawBounds(pixmap.DrawBounds()); break;
            default: break;
        }
        if (this.phase == Def.Phase.Play && mission > 0) {
            startMission(mission);
        }
    }

    private void MemorizeGamerProgress() {
        gameData.SetNbVies(decor.NbVies());
        decor.MemorizeDoors(gameData);
        gameData.Write();
    }

    public void ToggleFullScreen() {
        if (Gdx.graphics.isFullscreen()) {
            Gdx.graphics.setWindowedMode(640, 480);
        } else {
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        }
    }

    public boolean IsFullScreen() {
        return Gdx.graphics.isFullscreen();
    }

    public GraphicsDeviceManager GetGraphicsManager() {
        return graphics;
    }
}
