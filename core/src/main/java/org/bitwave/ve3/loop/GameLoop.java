package org.bitwave.ve3.loop;

import org.bitwave.ve3.UpdateContext;

/**
 * Contract implemented by a game or engine backend.
 */
public interface GameLoop {
    void init();
    boolean shouldContinue();
    void update(UpdateContext context);
    void render();
    double pollDeltaTime();
    void shutdown();
}
