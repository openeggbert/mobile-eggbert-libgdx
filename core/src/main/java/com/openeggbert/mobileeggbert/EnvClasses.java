package com.openeggbert.mobileeggbert;

public class EnvClasses {

    public enum Platform {
        Desktop, Android, iOS, Web;

        public boolean IsDesktop()    { return this == Desktop; }
        public boolean IsAndroid()    { return this == Android; }
        public boolean IsIOS()        { return this == iOS; }
        public boolean IsWeb()        { return this == Web; }
        public boolean IsNotDesktop() { return this != Desktop; }
        public boolean IsNotAndroid() { return this != Android; }
        public boolean IsNotIOS()     { return this != iOS; }
        public boolean IsNotWeb()     { return this != Web; }
    }

    public enum Impl {
        MonoGame, FNA, KNI, JXNA, JSXNA;

        public boolean IsMonoGame()    { return this == MonoGame; }
        public boolean IsFNA()         { return this == FNA; }
        public boolean IsKNI()         { return this == KNI; }
        public boolean IsJXNA()        { return this == JXNA; }
        public boolean IsJSXNA()       { return this == JSXNA; }
        public boolean IsNotMonoGame() { return this != MonoGame; }
        public boolean IsNotFNA()      { return this != FNA; }
        public boolean IsNotKNI()      { return this != KNI; }
        public boolean IsNotJXNA()     { return this != JXNA; }
        public boolean IsNotJSXNA()    { return this != JSXNA; }
    }

    public enum ProgrammingLanguage {
        CSharp, Java, JavaScript
    }

    public static ProgrammingLanguage GetProgrammingLanguage(Impl impl) {
        switch (impl) {
            case MonoGame: return ProgrammingLanguage.CSharp;
            case FNA:      return ProgrammingLanguage.CSharp;
            case KNI:      return ProgrammingLanguage.CSharp;
            case JXNA:     return ProgrammingLanguage.Java;
            case JSXNA:    return ProgrammingLanguage.JavaScript;
            default: throw new RuntimeException("Unsupported Impl: " + impl);
        }
    }
}
