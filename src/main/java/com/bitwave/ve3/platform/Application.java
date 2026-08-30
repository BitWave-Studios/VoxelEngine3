package com.bitwave.ve3.platform;

/**
 * Interface for application management (window, render, scene updates and shutdown).
 * @apiNote Contract for anything running inside the loop
 */
public interface Application {
    void init(Window window);
    void fixedUpdate(float fixedDelta); // simulation tick, fixed timestep
    void render(float alpha); // alpha is the interpolation factor between the last two sim ticks
    void shutdown();
}
