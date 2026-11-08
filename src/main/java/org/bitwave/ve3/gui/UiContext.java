package org.bitwave.ve3.gui;

import java.util.HashMap;
import java.util.Map;

public final class UiContext {
    public final UiInput input = new UiInput();
    public final UiDrawList drawList = new UiDrawList();

    private final Map<Long, Object> widgetState = new HashMap<>();

    private long hotId;
    private long activeId;
    private long focusedId;

    private float width;
    private float height;

    public void beginFrame(float width, float height) {
        this.width = width;
        this.height = height;

        drawList.clear();
        input.beginFrame();

        hotId = 0;
        activeId = 0;
        focusedId = 0;
    }

    public void endFrame() {
        input.endFrame();
    }

    public float width() { return width; }
    public float height() { return height; }
    public long hotId() { return hotId; }
    public long activeId() { return activeId; }
    public long focusedId() { return focusedId; }

    public void setHot(long id) { hotId = id; }
    public void setActive(long id) { activeId = id; }
    public void setFocused(long id) { focusedId = id; }

    public boolean isActive(long id) { return activeId == id; }
    public boolean isFocused(long id) { return focusedId == id; }

    public boolean isMouseCaptured() { return activeId != 0; }

    public <T> T state(long id, Class<T> type, StateFactory<T> factory) {
        Object value = widgetState.get(id);

        if (value == null) {
            value = factory.create();
            widgetState.put(id, value);
        }

        return (T) value;
    }

    public interface StateFactory<T> {
        T create();
    }
}
