package org.bitwave.ve3.gui;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class UiContext {
    private static final long STALE_STATE_FRAMES = 120;

    public final UiInput input = new UiInput();
    public final UiDrawList drawList = new UiDrawList();

    private final Map<UiId, UiStateEntry> widgetState = new HashMap<>();
    private final ArrayDeque<UiId> idStack = new ArrayDeque<>();

    private UiId hotId = UiId.NONE;
    private UiId activeId = UiId.NONE;
    private UiId focusedId = UiId.NONE;

    private float width;
    private float height;
    private long frameStamp;

    public void beginFrame(float width, float height) {
        this.width = width;
        this.height = height;

        drawList.clear();
        hotId = UiId.NONE;

        idStack.clear();
        idStack.push(UiId.NONE);

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
    public UiId hotId() { return hotId; }
    public UiId activeId() { return activeId; }
    public UiId focusedId() { return focusedId; }

    public void setHot(UiId id) { hotId = id; }
    public void setActive(UiId id) { activeId = id; }
    public void setFocused(UiId id) { focusedId = id; }

    public boolean isHot(UiId id) { return hotId == id; }
    public boolean isActive(UiId id) { return activeId == id; }
    public boolean isFocused(UiId id) { return focusedId == id; }

    public void clearActive() { activeId = UiId.NONE; }
    public void clearFocused() { focusedId = UiId.NONE; }

    public boolean isMouseCaptured() { return activeId != UiId.NONE; }

    public void pushId(UiId id) {
        idStack.push(requireId(id));
    }

    public void popId() {
        if (idStack.size() > 1) {
            idStack.pop();
        }
    }

    public UiId scopedId(String name) {
        UiId parent = idStack.peek();
        if (parent == null) parent = UiId.NONE;
        return parent.child(name);
    }

    public <T> T state(UiId id, Class<T> type, StateFactory<T> factory) {
        requireId(id);

        UiStateEntry entry = widgetState.computeIfAbsent(id, key -> new UiStateEntry(frameStamp, factory.create()));

        Object value = entry.value();

        if (!type.isInstance(value)) {
            throw new IllegalStateException("UI state type mismatch for id " + id);
        }

        entry.markSeen(frameStamp);
        return type.cast(value);
    }

    private void pruneStaleState() {
        widgetState.entrySet().removeIf(entry ->
                frameStamp - entry.getValue().lastSeenFrame() > STALE_STATE_FRAMES);
    }

    private static UiId requireId(UiId id) {
        return Objects.requireNonNull(id, "id");
    }

    public interface StateFactory<T> {
        T create();
    }

    private static final class UiStateEntry {
        private Object value;
        private long lastSeenFrame;

        private UiStateEntry(long frame, Object value) {
            this.value = value;
            this.lastSeenFrame = frame;
        }

        private Object value() {
            return value;
        }

        private long lastSeenFrame() {
            return lastSeenFrame;
        }

        private void markSeen(long frame) {
            lastSeenFrame = frame;
        }
    }
}
