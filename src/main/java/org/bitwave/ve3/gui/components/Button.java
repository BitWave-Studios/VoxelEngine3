package org.bitwave.ve3.gui.components;

import org.bitwave.ve3.gui.*;

/**
 * A simple button component for the UI system.
 */
public final class Button {
    private final UiContext context;

    public Button(UiContext context) {
        this.context = context;
    }

    public UiResponse draw(UiId id, UiFont font, String label, float x, float y, float width, float height, boolean enabled) {
        UiRect rect = new UiRect(x, y, width, height);

        boolean hovered = enabled && rect.contains(
                context.input.mouseX,
                context.input.mouseY
        );

        if (hovered) {
            context.setHot(id);
        }

        if (hovered && context.input.wasMousePressed(0)) {
            context.setActive(id);
        }

        boolean wasActive = context.isActive(id);
        boolean active = wasActive && context.input.isMouseDown(0);
        boolean clicked = enabled
                && wasActive
                && context.input.wasMouseReleased(0)
                && hovered;

        if (wasActive && context.input.wasMouseReleased(0)) {
            context.clearActive();
        }

        UiColor background = !enabled || active
                ? UiColor.rgba(45, 45, 55, 1)
                : hovered
                ? UiColor.rgba(75 ,75, 88, 1)
                : UiColor.rgba(60, 60, 70, 1);

        context.drawList.addFilledRect(rect, background, 10f);

        if (font != null) {
            float fontSize = 14f;
            float lineHeight = font.lineHeight(fontSize);
            float baselineY = y + (height - lineHeight) * 0.5f + lineHeight * 0.8f;

            context.drawList.addText(
                    font, x + 10f, baselineY, label,
                    enabled ? UiColor.WHITE : UiColor.GRAY,
                    fontSize
            );
        }

        return new UiResponse(id, hovered, active, clicked);
    }
}
