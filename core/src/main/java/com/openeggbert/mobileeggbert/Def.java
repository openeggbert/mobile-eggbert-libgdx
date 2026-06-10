package com.openeggbert.mobileeggbert;

public class Def {

    public enum Phase {
        None, First, Wait, Init, Play, Pause, Lost, Win,
        Trial, MainSetup, PlaySetup, Resume, Ranking
    }

    public enum ButtonGlyph {
        None,
        InitGamerA, InitGamerB, InitGamerC,
        InitSetup, InitPlay, InitBuy, InitRanking,
        WinLostReturn,
        TrialBuy, TrialCancel,
        SetupSounds, SetupJump, SetupZoom, SetupAccel, SetupReset, SetupReturn,
        PauseMenu, PauseBack, PauseSetup, PauseRestart, PauseContinue,
        PlayPause, PlayJump, PlayAction, PlayDown,
        ResumeMenu, ResumeContinue,
        RankingContinue,
        Cheat11, Cheat12, Cheat21, Cheat22, Cheat31, Cheat32,
        Cheat1, Cheat2, Cheat3, Cheat4, Cheat5, Cheat6, Cheat7, Cheat8, Cheat9
    }

    public static final int LXIMAGE = 640;
    public static final int LYIMAGE = 480;
    public static final int MAXCELX = 100;
    public static final int MAXCELY = 100;
    public static final int DIMOBJX = 64;
    public static final int DIMOBJY = 64;
    public static final int DIMBLUPIX = 60;
    public static final int DIMBLUPIY = 60;
    public static final int DIMEXPLOX = 128;
    public static final int DIMEXPLOY = 128;
    public static final int DIMBUTTONX = 40;
    public static final int DIMBUTTONY = 40;
    public static final int DIMJAUGEX = 124;
    public static final int DIMJAUGEY = 22;
    public static final int POSSTATX = 12;
    public static final int POSSTATY = 220;
    public static final int DIMSTATX = 60;
    public static final int DIMSTATY = 30;
    public static final int DIMTEXTX = 32;
    public static final int DIMTEXTY = 32;

    public static final int CHOBJECT = 1;
    public static final int CHBLUPI = 2;
    public static final int CHDECOR = 3;
    public static final int CHBUTTON = 4;
    public static final int CHJAUGE = 5;
    public static final int CHTEXT = 6;
    public static final int CHEXPLO = 9;
    public static final int CHELEMENT = 10;
    public static final int CHBLUPI1 = 11;
    public static final int CHBLUPI2 = 12;
    public static final int CHBLUPI3 = 13;
    public static final int CHPAD = 14;
    public static final int CHSPEEDYBLUPI = 15;
    public static final int CHBLUPIYOUPIE = 16;
    public static final int CHGEAR = 17;

    public static final int ACTION_STOP = 1;
    public static final int ACTION_MARCH = 2;
    public static final int ACTION_TURN = 3;
    public static final int ACTION_JUMP = 4;
    public static final int ACTION_AIR = 5;
    public static final int ACTION_DOWN = 6;
    public static final int ACTION_UP = 7;
    public static final int ACTION_VERTIGO = 8;
    public static final int ACTION_RECEDE = 9;
    public static final int ACTION_ADVANCE = 10;
    public static final int ACTION_CLEAR1 = 11;
    public static final int ACTION_SET = 12;
    public static final int ACTION_WIN = 13;
    public static final int ACTION_PUSH = 14;
    public static final int ACTION_STOPHELICO = 15;
    public static final int ACTION_MARCHHELICO = 16;
    public static final int ACTION_TURNHELICO = 17;
    public static final int ACTION_STOPNAGE = 18;
    public static final int ACTION_MARCHNAGE = 19;
    public static final int ACTION_TURNNAGE = 20;
    public static final int ACTION_STOPSURF = 21;
    public static final int ACTION_MARCHSURF = 22;
    public static final int ACTION_TURNSURF = 23;
    public static final int ACTION_DROWN = 24;
    public static final int ACTION_STOPJEEP = 25;
    public static final int ACTION_MARCHJEEP = 26;
    public static final int ACTION_TURNJEEP = 27;
    public static final int ACTION_STOPPOP = 28;
    public static final int ACTION_POP = 29;
    public static final int ACTION_BYE = 30;
    public static final int ACTION_STOPSUSPEND = 31;
    public static final int ACTION_MARCHSUSPEND = 32;
    public static final int ACTION_TURNSUSPEND = 33;
    public static final int ACTION_JUMPSUSPEND = 34;
    public static final int ACTION_HIDE = 35;
    public static final int ACTION_JUMPAIE = 36;
    public static final int ACTION_STOPSKATE = 37;
    public static final int ACTION_MARCHSKATE = 38;
    public static final int ACTION_TURNSKATE = 39;
    public static final int ACTION_JUMPSKATE = 40;
    public static final int ACTION_AIRSKATE = 41;
    public static final int ACTION_TAKESKATE = 42;
    public static final int ACTION_DEPOSESKATE = 43;
    public static final int ACTION_OUF1a = 44;
    public static final int ACTION_OUF1b = 45;
    public static final int ACTION_OUF2 = 46;
    public static final int ACTION_OUF3 = 47;
    public static final int ACTION_OUF4 = 48;
    public static final int ACTION_SUCETTE = 49;
    public static final int ACTION_STOPTANK = 50;
    public static final int ACTION_MARCHTANK = 51;
    public static final int ACTION_TURNTANK = 52;
    public static final int ACTION_FIRETANK = 53;
    public static final int ACTION_GLU = 54;
    public static final int ACTION_DRINK = 55;
    public static final int ACTION_CHARGE = 56;
    public static final int ACTION_ELECTRO = 57;
    public static final int ACTION_HELICOGLU = 58;
    public static final int ACTION_TURNAIR = 59;
    public static final int ACTION_STOPMARCH = 60;
    public static final int ACTION_STOPJUMP = 61;
    public static final int ACTION_STOPJUMPh = 62;
    public static final int ACTION_MOCKERY = 63;
    public static final int ACTION_MOCKERYi = 64;
    public static final int ACTION_OUF5 = 65;
    public static final int ACTION_BALLOON = 66;
    public static final int ACTION_STOPOVER = 67;
    public static final int ACTION_MARCHOVER = 68;
    public static final int ACTION_TURNOVER = 69;
    public static final int ACTION_RECEDEq = 70;
    public static final int ACTION_ADVANCEq = 71;
    public static final int ACTION_STOPECRASE = 72;
    public static final int ACTION_MARCHECRASE = 73;
    public static final int ACTION_TELEPORTE = 74;
    public static final int ACTION_CLEAR2 = 75;
    public static final int ACTION_CLEAR3 = 76;
    public static final int ACTION_CLEAR4 = 77;
    public static final int ACTION_CLEAR5 = 78;
    public static final int ACTION_CLEAR6 = 79;
    public static final int ACTION_CLEAR7 = 80;
    public static final int ACTION_CLEAR8 = 81;
    public static final int ACTION_SWITCH = 82;
    public static final int ACTION_MOCKERYp = 83;
    public static final int ACTION_NON = 84;
    public static final int ACTION_SLOWDOWNSKATE = 85;
    public static final int ACTION_TAKEDYNAMITE = 86;
    public static final int ACTION_PUTDYNAMITE = 87;

    public static final int DIR_LEFT = 1;
    public static final int DIR_RIGHT = 2;

    public static final int SEC_SHIELD = 1;
    public static final int SEC_POWER = 2;
    public static final int SEC_CLOUD = 3;
    public static final int SEC_HIDE = 4;

    public static final int TYPE_ASCENSEUR = 1;
    public static final int TYPE_BOMBEDOWN = 2;
    public static final int TYPE_BOMBEUP = 3;
    public static final int TYPE_BULLDOZER = 4;
    public static final int TYPE_TRESOR = 5;
    public static final int TYPE_EGG = 6;
    public static final int TYPE_GOAL = 7;
    public static final int TYPE_EXPLO1 = 8;
    public static final int TYPE_EXPLO2 = 9;
    public static final int TYPE_EXPLO3 = 10;
    public static final int TYPE_EXPLO4 = 11;
    public static final int TYPE_CAISSE = 12;
    public static final int TYPE_HELICO = 13;
    public static final int TYPE_PLOUF = 14;
    public static final int TYPE_BLUP = 15;
    public static final int TYPE_BOMBEMOVE = 16;
    public static final int TYPE_POISSON = 17;
    public static final int TYPE_TOMATES = 18;
    public static final int TYPE_JEEP = 19;
    public static final int TYPE_OISEAU = 20;
    public static final int TYPE_CLE = 21;
    public static final int TYPE_DOOR = 22;
    public static final int TYPE_BALLE = 23;
    public static final int TYPE_SKATE = 24;
    public static final int TYPE_SHIELD = 25;
    public static final int TYPE_POWER = 26;
    public static final int TYPE_MAGICTRACK = 27;
    public static final int TYPE_TANK = 28;
    public static final int TYPE_BULLET = 29;
    public static final int TYPE_DRINK = 30;
    public static final int TYPE_CHARGE = 31;
    public static final int TYPE_BLUPIHELICO = 32;
    public static final int TYPE_BLUPITANK = 33;
    public static final int TYPE_GLU = 34;
    public static final int TYPE_TIPLOUF = 35;
    public static final int TYPE_POLLUTION = 36;
    public static final int TYPE_CLEAR = 37;
    public static final int TYPE_ELECTRO = 38;
    public static final int TYPE_TRESORTRACK = 39;
    public static final int TYPE_INVERT = 40;
    public static final int TYPE_INVERTSTART = 41;
    public static final int TYPE_INVERTSTOP = 42;
    public static final int TYPE_GUEPE = 44;
    public static final int TYPE_OVER = 46;
    public static final int TYPE_ASCENSEURs = 47;
    public static final int TYPE_ASCENSEURsi = 48;
    public static final int TYPE_CLE1 = 49;
    public static final int TYPE_CLE2 = 50;
    public static final int TYPE_CLE3 = 51;
    public static final int TYPE_BRIDGE = 52;
    public static final int TYPE_TENTACULE = 53;
    public static final int TYPE_CREATURE = 54;
    public static final int TYPE_DYNAMITE = 55;
    public static final int TYPE_DYNAMITEf = 56;
    public static final int TYPE_SHIELDTRACK = 57;
    public static final int TYPE_HIDETRACK = 58;
    public static final int TYPE_EXPLO5 = 90;
    public static final int TYPE_EXPLO6 = 91;
    public static final int TYPE_EXPLO7 = 92;
    public static final int TYPE_EXPLO8 = 93;
    public static final int TYPE_EXPLO9 = 94;
    public static final int TYPE_EXPLO10 = 95;
    public static final int TYPE_BOMBEFOLLOW1 = 96;
    public static final int TYPE_BOMBEFOLLOW2 = 97;
    public static final int TYPE_SPLOUTCH1 = 98;
    public static final int TYPE_SPLOUTCH2 = 99;
    public static final int TYPE_SPLOUTCH3 = 100;
    public static final int TYPE_BOMBEPERSO1 = 200;
    public static final int TYPE_BOMBEPERSO2 = 201;
    public static final int TYPE_BOMBEPERSO3 = 202;
    public static final int TYPE_BOMBEPERSO4 = 203;

    public static final int STEP_STOPSTART = 1;
    public static final int STEP_ADVANCE = 2;
    public static final int STEP_STOPEND = 3;
    public static final int STEP_RECEDE = 4;

    public static final int DECOR_EXPLO1 = 1;
    public static final int DECOR_EXPLO2 = 2;
    public static final int DECOR_EXPLO3 = 3;
    public static final int DECOR_EXPLO4 = 4;
    public static final int DECOR_BALLOON = 5;

    public static final int SOUND_CLICK = 0;
    public static final int SOUND_JUMP1 = 1;
    public static final int SOUND_JUMP2 = 2;
    public static final int SOUND_JUMPEND = 3;
    public static final int SOUND_JUMPTOC = 4;
    public static final int SOUND_TURN = 5;
    public static final int SOUND_VERTIGO = 6;
    public static final int SOUND_DOWN = 7;
    public static final int SOUND_FALL = 8;
    public static final int SOUND_NEW = 9;
    public static final int SOUND_BOUM = 10;
    public static final int SOUND_TRESOR = 11;
    public static final int SOUND_EGG = 12;
    public static final int SOUND_ENDKO = 13;
    public static final int SOUND_ENDOK = 14;
    public static final int SOUND_HELICOSTART = 15;
    public static final int SOUND_HELICOHIGH = 16;
    public static final int SOUND_HELICOSTOP = 17;
    public static final int SOUND_HELICOLOW = 18;
    public static final int SOUND_LASTTRESOR = 19;
    public static final int SOUND_UP = 20;
    public static final int SOUND_LOOKUP = 21;
    public static final int SOUND_JUMP0 = 22;
    public static final int SOUND_PLOUF = 23;
    public static final int SOUND_BLUP = 24;
    public static final int SOUND_SURF = 25;
    public static final int SOUND_DROWN = 26;
    public static final int SOUND_ERROR = 27;
    public static final int SOUND_JEEPSTART = 28;
    public static final int SOUND_JEEPHIGH = 29;
    public static final int SOUND_JEEPSTOP = 30;
    public static final int SOUND_JEEPLOW = 31;
    public static final int SOUND_BYE = 32;
    public static final int SOUND_DOOR = 33;
    public static final int SOUND_SUSPENDTOC = 34;
    public static final int SOUND_SUSPENDJUMP = 35;
    public static final int SOUND_SINGE = 36;
    public static final int SOUND_PATIENT = 37;
    public static final int SOUND_PUSH = 38;
    public static final int SOUND_POP = 39;
    public static final int SOUND_JUMPAIE = 40;
    public static final int SOUND_RESSORT = 41;
    public static final int SOUND_STARTSHIELD = 42;
    public static final int SOUND_STOPSHIELD = 43;
    public static final int SOUND_STARTPOWER = 44;
    public static final int SOUND_STOPPOWER = 45;
    public static final int SOUND_OUF1 = 46;
    public static final int SOUND_OUF2 = 47;
    public static final int SOUND_OUF3 = 48;
    public static final int SOUND_OUF4 = 49;
    public static final int SOUND_SUCETTE = 50;
    public static final int SOUND_GLU = 51;
    public static final int SOUND_FIREOK = 52;
    public static final int SOUND_FIREKO = 53;
    public static final int SOUND_TAKEGLU = 54;
    public static final int SOUND_STARTCLOUD = 55;
    public static final int SOUND_STOPCLOUD = 56;
    public static final int SOUND_DRINK = 57;
    public static final int SOUND_CHARGE = 58;
    public static final int SOUND_ELECTRO = 59;
    public static final int SOUND_PERSOTAKE = 60;
    public static final int SOUND_PERSOPOSE = 61;
    public static final int SOUND_STARTHIDE = 62;
    public static final int SOUND_STOPHIDE = 63;
    public static final int SOUND_TIPLOUF = 64;
    public static final int SOUND_MOCKERY = 65;
    public static final int SOUND_INVERTSTART = 66;
    public static final int SOUND_INVERTSTOP = 67;
    public static final int SOUND_OVERSTOP = 68;
    public static final int SOUND_BLITZ = 69;
    public static final int SOUND_ECRASE = 70;
    public static final int SOUND_TELEPORTE = 71;
    public static final int SOUND_BRIDGE1 = 72;
    public static final int SOUND_BRIDGE2 = 73;
    public static final int SOUND_ANGEL = 74;
    public static final int SOUND_SCIE = 75;
    public static final int SOUND_SWITCHOFF = 76;
    public static final int SOUND_SWITCHON = 77;
    public static final int SOUND_JUMPENDb = 78;
    public static final int SOUND_JUMPTOCb = 79;
    public static final int SOUND_JUMPENDm = 80;
    public static final int SOUND_JUMPTOCm = 81;
    public static final int SOUND_JUMPENDg = 82;
    public static final int SOUND_JUMPTOCg = 83;
    public static final int SOUND_JUMPENDo = 84;
    public static final int SOUND_JUMPTOCo = 85;
    public static final int SOUND_JUMPENDk = 86;
    public static final int SOUND_JUMPTOCk = 87;
    public static final int SOUND_JUMPENDf = 88;
    public static final int SOUND_JUMPTOCf = 89;
    public static final int SOUND_JUMPENDh = 90;
    public static final int SOUND_JUMPTOCh = 91;
    public static final int SOUND_FOLLOW = 92;

    public static final int KEY_JUMP = 1;
    public static final int KEY_FIRE = 2;
    public static final int KEY_DOWN = 4;

    public static boolean HasSound = true;
    public static boolean EasyMove = true;
}
