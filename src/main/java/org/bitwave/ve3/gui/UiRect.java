package org.bitwave.ve3.gui;

/**
 * A simple rectangle class for UI elements.
 * <p>
 *     It is used to define the position and size of UI elements in the UI context.
 * </p>
 * @apiNote This class is immutable and thread-safe.
 * @param x The x-coordinate of the rectangle's top-left corner.
 * @param y The y-coordinate of the rectangle's top-left corner.
 * @param width The width of the rectangle.
 * @param height The height of the rectangle.
 */
public record UiRect(float x, float y, float width, float height) {
    public boolean contains(float px, float py) {
        return px >= x
                && px < x + width
                && py >= y
                && py < y + height;
    }

    public UiRect inset(float amount) {
        return new UiRect(
                x + amount,
                y + amount,
                Math.max(0, width - amount * 2),
                Math.max(0, height - amount * 2)
        );
    }
}
