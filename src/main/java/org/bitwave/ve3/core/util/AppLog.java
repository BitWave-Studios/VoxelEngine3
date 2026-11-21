package org.bitwave.ve3.core.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class AppLog {
    private static final ConcurrentLinkedQueue<String> LINES = new ConcurrentLinkedQueue<>();

    private AppLog() {}

    public static void info(String message) {
        LINES.add(Instant.now() + " INFO: " + message);
    }

    public static void error(String message, Throwable error) {
        LINES.add(Instant.now() + " ERROR: " + message + ":" + error.getMessage());
    }

    public static void writeTo(Path path) throws IOException {
        Files.write(
                path,
                LINES,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
        );
    }
}
