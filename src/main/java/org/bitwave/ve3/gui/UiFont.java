package org.bitwave.ve3.gui;

public interface UiFont {
    UiTexture atlas();
    float lineHeight(float fontSize);
    float advance(char character, float fontSize);
    UiGlyph glyph(char character, float fontSize);
}
