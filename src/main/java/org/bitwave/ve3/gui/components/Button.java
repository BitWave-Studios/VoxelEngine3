package org.bitwave.ve3.gui.components;

import org.bitwave.ve3.gui.UiColor;
import org.bitwave.ve3.gui.UiContext;
import org.bitwave.ve3.gui.UiRect;

/**
 * A simple button component for the UI system.
 */
public final class Button {
    private final UiContext context;

    public Button(UiContext context) {
        this.context = context;
    }

    public boolean draw(String label, float x, float y, float width, float height) {
        long id = hash(label);
        UiRect rect = new UiRect(x, y, width, height);

        boolean hovered = rect.contains(
                context.input.mouseX,
                context.input.mouseY
        );

        boolean pressed = hovered && context.input.wasMousePressed(0);

        if (hovered) context.setActive(id);
        if (pressed) context.setFocused(id);

        UiColor backgroundColor = UiColor.rgba(60, 60, 70, 255);

        if (context.isActive(id)) {
            backgroundColor = UiColor.rgba(45, 45, 55, 255);
        } else if (hovered) {
            backgroundColor = UiColor.rgba(75, 75, 88, 255);
        }

        context.drawList.addFilledRect(rect, backgroundColor);
        context.drawList.addText(x + 10, y + height * 0.5f - 7, label, UiColor.WHITE, 14);

        return pressed;
    }

    private long hash(String value) {
        return value.hashCode() & 0xFFFFFFFFL;
    }
}
