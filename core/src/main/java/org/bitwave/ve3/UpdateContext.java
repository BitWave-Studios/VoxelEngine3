package org.bitwave.ve3;

/**
 * Immutable timing data passed to a single engine update.
 * @param deltaTime elapsed time since the previous update, in seconds
 * @param frameNumber zero-based index of the current engine frame
 */
public record UpdateContext(double deltaTime, long frameNumber) {
    public UpdateContext {
        if (!Double.isFinite(deltaTime) || deltaTime < 0.0) {
            throw new IllegalArgumentException("Delta time must be a non-negative finite value");
        }
    }

    public static UpdateContext initial() {
        return new UpdateContext(0.0, 0L);
    }
}
