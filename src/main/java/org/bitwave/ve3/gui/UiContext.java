package org.bitwave.ve3.gui;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;

public final class UiContext {
    public static final long NO_ID = 0L;

    public final UiInput input = new UiInput();
    public final UiDrawList drawList = new UiDrawList();

    private final Map<Long, UiStateEntry> widgetState = new HashMap<>();
    private final ArrayDeque<Long> idStack = new ArrayDeque<>();

    private long hotId = NO_ID;
    private long activeId = NO_ID;
    private long focusedId = NO_ID;

    private float width;
    private float height;

    public void beginFrame(float width, float height) {
        this.width = width;
        this.height = height;

        drawList.clear();
        input.beginFrame();

        hotId = NO_ID;

        idStack.clear();
        idStack.push(NO_ID);

        frameStamp++;
    }

    public void endFrame() {
        input.endFrame();

        // If no widget claimed hover this frame, there is no hot widget.
        // Active/focused remain unchanged unless a widget explicitly changes them.

        pruneStaleState();
    }

    public float width() { return width; }
    public float height() { return height; }
    public long hotId() { return hotId; }
    public long activeId() { return activeId; }
    public long focusedId() { return focusedId; }

    public void setHot(long id) { hotId = id; }
    public void setActive(long id) { activeId = id; }
    public void setFocused(long id) { focusedId = id; }

    public boolean isHot(long id) { return hotId == id; }
    public boolean isActive(long id) { return activeId == id; }
    public boolean isFocused(long id) { return focusedId == id; }

    public void clearActive() { activeId = NO_ID; }
    public void clearFocused() { focusedId = NO_ID; }

    public boolean isMouseCaptured() { return activeId != NO_ID; }

    public void pushId(long id) { idStack.push(id); }
    public void popId() {
        if (idStack.size() > 10) {
            idStack.pop();
        }
    }

    public long scopedId(String name) {
        long parent = idStack.peek() == null ? NO_ID : idStack.peek();
        return Hashing.combine(parent, Hashing.hash64(name));
    }

    public <T> T state(long id, Class<T> type, StateFactory<T> factory) {
        UiStateEntry entry = widgetState.computeIfAbsent(id, key -> new UiStateEntry(frameStamp, factory.create()));

        Object value = entry.value();

        if (!type.isInstance(value)) {
            throw new IllegalStateException("UI state type mismatch for id " + id);
        }

        entry.markSeen(frameStamp);
        return type.cast(value);
    }

    private long frameStamp;

    private void pruneStaleState() {
        // Remove entries not accessed for N frames to prevent unbounded growth of the state map.

    }

    public interface StateFactory<T> {
        T create();
    }

    private static final class UiStateEntry {
        private Object value;
        private long lastSeenFrame;

        private UiStateEntry(Object value, long frame) {
            this.value = value;
            this.lastSeenFrame = frame;
        }

        private Object value() {
            return value;
        }

        private void markSeen(long frame) {
            lastSeenFrame = frame;
        }
    }
}
