package com.bitwave.ve3.platform;

public record WindowConfig(
        int width,
        int height,
        String title,
        boolean resizeable,
        boolean vsync
) {
    public static WindowConfig defaults() {
        return new WindowConfig(1280, 720, "VE3", true, true);
    }
}
