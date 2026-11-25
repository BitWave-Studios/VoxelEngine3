package org.bitwave.ve3.gui;

public record UiGlyph(
        float width,
        float height,
        float bearingX,
        float bearingY,
        float advance,
        float u0,
        float v0,
        float u1,
        float v1
) {}
