package org.bitwave.ve3;

import org.bitwave.ve3.loop.GameLoop;

/**
 * Top-level engine entry point.
 *
 * <p>The engine owns the high-level lifecycle - initialisation, update/render loop and orderly shutdown.
 * Platform backends supply the window, input and renderer implementations.</p>
 */
public final class Engine implements AutoCloseable {
    private final GameLoop loop;
    private boolean running;
    private long frameNumber;

    public Engine(GameLoop loop) {
        this.loop = loop;
    }

    public void start() throws Exception {
        if (running) {
            throw new IllegalStateException("Engine is already running");
        }

        running = true;
        frameNumber = 0L;

        loop.init();

        try {
            while (running && loop.shouldContinue()) {
                UpdateContext context = new UpdateContext(
                        loop.pollDeltaTime(),
                        frameNumber
                );
                loop.update(context);
                loop.render();
            }
        } finally {
            close();
        }
    }

    public void stop() {
        running = false;
    }

    @Override
    public void close() {
        if (running) {
            running = false;
        }
        loop.shutdown();
    }
}
