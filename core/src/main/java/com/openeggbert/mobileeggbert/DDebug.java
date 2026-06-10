package com.openeggbert.mobileeggbert;

public class DDebug {
    private static final boolean detailedDebugging = false;

    public static void WriteLine(String msg) {
        if (detailedDebugging) {
            System.out.println(msg);
        }
    }
}
