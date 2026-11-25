package org.bitwave.ve3.gui;

/**
 * This interface represents a command to draw something in the UI.
 * It is sealed, meaning that only the classes defined within this file can implement it.
 * This allows for a controlled set of drawing commands, which can be useful for rendering and managing UI elements.
 */
public sealed interface UiDrawCommand {
    record FilledRect(UiRect rect, UiColor color, float cornerRadius) implements UiDrawCommand {}

    record TexturedRect(UiRect rect, UiTexture texture, UiColor tint) implements UiDrawCommand {}

    record Text(UiFont font, float x, float y, String value, UiColor color, float fontSize) implements UiDrawCommand {}

    record Line(float x1, float y1, float x2, float y2, UiColor color, float thickness) implements UiDrawCommand {}

    record PushClip(UiRect rect) implements UiDrawCommand {}

    record PopClip() implements UiDrawCommand {}
}
