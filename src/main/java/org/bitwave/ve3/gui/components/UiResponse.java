package org.bitwave.ve3.gui.components;

import org.bitwave.ve3.gui.UiId;

public record UiResponse(UiId id, boolean hovered, boolean active, boolean clicked) {
    public boolean pressed() {
        return clicked;
    }
}
