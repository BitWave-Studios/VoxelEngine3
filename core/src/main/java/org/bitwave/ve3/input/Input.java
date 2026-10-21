package org.bitwave.ve3.input;

/**
 * Read-only input state exposed to games and engine systems.
 *
 * <p>Implementations should update their internal state once per frame, before
 * gameplay updates run.</p>
 */
public interface Input {
    /**
     * Returns whether a key is currently down.
     * @param key the key to check
     * @return true if the key is currently down, false otherwise
     */
    boolean isKeyDown(Key key);

    /**
     * Returns whether a key was pressed during the current frame.
     * @param key the key to check
     * @return true if the key was pressed during this frame, false otherwise
     */
    boolean wasKeyPressed(Key key);

    /**
     * Returns whether a key was released during the current frame.
     * @param key the key to check
     * @return true if the key was released during this frame, false otherwise
     */
    boolean wasKeyReleased(Key key);
}
