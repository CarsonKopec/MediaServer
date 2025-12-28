package dev.imagineforgee.ms.server.media;

import dev.imagineforgee.ms.server.util.OS;

import java.util.List;

public final class StreamCommandBuilder {

    public static List<String> build(String input, boolean shouldCache) {
        return switch (OS.current()) {
            case WINDOWS -> windows(input, shouldCache);
            case LINUX   -> linux(input, shouldCache);
            case MAC     -> mac(input, shouldCache);
            default      -> throw new IllegalStateException("Unsupported OS");
        };
    }

    private static List<String> windows(String input, boolean shouldCache) {

        return List.of(
                "wsl","cvlc",
                shellQuote(input),
                "--intf", "dummy",
                shellQuote(shouldCache(shouldCache)),
                "--no-sout-all",
                "--sout-keep"
        );
    }

    private static List<String> linux(String input, boolean shouldCache) {
        return List.of(
                "cvlc",
                input,
                "--intf", "dummy",
                shouldCache(shouldCache),
                "--no-sout-all",
                "--sout-keep"
        );
    }

    private static List<String> mac(String input, boolean shouldCache) {
        return List.of(
                "/Applications/VLC.app/Contents/MacOS/VLC",
                input,
                "--intf", "dummy",
                shouldCache(shouldCache),
                "--no-sout-all",
                "--sout-keep"
        );
    }

    private static String shouldCache(boolean shouldCache) {
        if (shouldCache) {
            return "--sout=#duplicate{"
                    + "dst=std{access=http,mux=ts,dst=:8081/stream.ts},"
                    + "dst=std{access=file,mux=ts,dst=/mnt/e/MediaServer/save.ts}"
                    + "}";
        } else {
            return "--sout=#standard{access=http,mux=ts,dst=:8081/stream.ts}";
        }
    }

    private static String shellQuote(String s) {
        return "'" + s.replace("'", "'\\''") + "'";
    }

}

