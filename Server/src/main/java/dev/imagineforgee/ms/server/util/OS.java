package dev.imagineforgee.ms.server.util;

public enum OS {
    WINDOWS,
    LINUX,
    MAC,
    UNKNOWN;

    public static OS current() {
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) return WINDOWS;
        if (os.contains("nux") || os.contains("nix")) return LINUX;
        if (os.contains("mac")) return MAC;
        return UNKNOWN;
    }
}

