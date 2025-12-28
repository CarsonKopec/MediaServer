package dev.imagineforgee.ms.server.media;

import dev.imagineforgee.ms.server.exceptions.MediaStartupException;
import dev.imagineforgee.ms.server.util.OS;

import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public final class MediaStreamer {

    public static final MediaStreamer INSTANCE = new MediaStreamer();

    private static final Set<String> ALLOWED_EXTENSIONS =
            Set.of("ts", "m3u8", "mp4");

    private Process process;

    private MediaStreamer() {}

    public synchronized void start(String input, boolean shouldCache) {
        if (process != null && process.isAlive()) return;

        Path mediaPath = validateLocalMediaPath(input);
        validateExtension(mediaPath);

        String mediaInput = mediaPath.toString();

        if (OS.current() == OS.WINDOWS) {
            mediaInput = toWslPath(mediaPath);
        }

        List<String> command = StreamCommandBuilder.build(mediaInput, shouldCache);

        try {
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.redirectErrorStream(true);
            process = pb.start();

            if (!process.waitFor(2, TimeUnit.SECONDS)) {
                return;
            }

            String output = new String(process.getInputStream().readAllBytes());
            throw new MediaStartupException(
                    "VLC exited immediately:\n" + output
            );

        } catch (Exception e) {
            stop();
            throw new RuntimeException("Failed to start media streaming", e);
        }
    }

    public boolean isRunning() {
        return process != null && process.isAlive();
    }

    public synchronized void stop() {
        if (process != null) {
            process.destroyForcibly();
            process = null;
        }
    }


    private static Path validateLocalMediaPath(String input) {
        input = input.trim();

        if ((input.startsWith("\"") && input.endsWith("\"")) ||
                (input.startsWith("'") && input.endsWith("'"))) {
            input = input.substring(1, input.length() - 1);
        }

        if (input.matches("^[a-zA-Z]+://.*")) {
            throw new IllegalArgumentException("URLs are not allowed");
        }

        Path path = Paths.get(input).toAbsolutePath().normalize();

        if (!Files.exists(path)) {
            throw new IllegalArgumentException("File does not exist: " + path);
        }

        if (!Files.isRegularFile(path)) {
            throw new IllegalArgumentException("Path must be a file: " + path);
        }

        return path;
    }

    private static void validateExtension(Path path) {
        String name = path.getFileName().toString().toLowerCase();
        int dot = name.lastIndexOf('.');
        if (dot == -1) {
            throw new IllegalArgumentException("File must have an extension");
        }

        String ext = name.substring(dot + 1);
        if (!ALLOWED_EXTENSIONS.contains(ext)) {
            throw new IllegalArgumentException("Unsupported extension: ." + ext);
        }
    }

    private static String toWslPath(Path windowsPath) {
        String path = windowsPath.toAbsolutePath().toString();
        char drive = Character.toLowerCase(path.charAt(0));
        String rest = path.substring(2).replace('\\', '/');
        return "/mnt/" + drive + rest;
    }
}
