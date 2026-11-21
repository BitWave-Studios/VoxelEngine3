package org.bitwave.ve3.gui;

public record UiColor(float r, float g, float b, float a) {
    public static final UiColor WHITE = new UiColor(1, 1, 1, 1);
    public static final UiColor BLACK = new UiColor(0, 0, 0, 1);

    public static UiColor rgba(int r, int g, int b, int a) {
        return new UiColor(r / 255f, g / 255f, b / 255f, a);
    }
}
