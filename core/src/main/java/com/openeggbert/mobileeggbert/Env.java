package com.openeggbert.mobileeggbert;

public class Env {
    public static EnvClasses.Platform PLATFORM;
    public static EnvClasses.Impl IMPL;
    public static boolean INITIALIZED = false;

    public static void Init(EnvClasses.Impl impl, EnvClasses.Platform platform) {
        if (INITIALIZED) {
            throw new RuntimeException("Env was already initialized. Cannot call init again.");
        }
        IMPL = impl;
        PLATFORM = platform;
        INITIALIZED = true;
    }
}
