package org.bitwave.ve3.gui;

public record UiResponse(UiId id, boolean hovered, boolean active, boolean clicked) {
    public boolean pressed() {
        return clicked;
    }
}
