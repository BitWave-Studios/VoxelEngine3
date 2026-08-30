package com.bitwave.ve3.platform;

import org.lwjgl.glfw.GLFW;

public class GameLoop {
    private static final float FIXED_TIMESTEP = 1.0f / 60.0f;

    /**
     * Clamp guards against "spiral of death" - if a frame takes too long, each new frame would be a sim tick behind
     */
    private static final float MAX_FRAME_TIME = 0.25f;

    /** Window instance used for the game */
    private final Window window;

    /** Application instance in the game */
    private final Application application;

    public GameLoop(Window window, Application application) {
        this.window = window;
        this.application = application;
    }

    /**
     * Initialises the {@link Window} instance created
     */
    public void run() {
        application.init(window);

        double previousTime = glfwTimeSeconds();
        float accumulator = 0f;

        while (!window.shouldClose()) {
            double currentTime = glfwTimeSeconds();
            float frameTime = (float) (currentTime - previousTime);
            previousTime = currentTime;

            if (frameTime > MAX_FRAME_TIME) {
                frameTime = MAX_FRAME_TIME;
            }

            accumulator += frameTime;

            window.pollEvents();

            while (accumulator >= FIXED_TIMESTEP) {
                application.fixedUpdate(FIXED_TIMESTEP);
                accumulator -= FIXED_TIMESTEP;
            }

            float alpha = accumulator / FIXED_TIMESTEP;
            application.render(alpha);

            window.swapBuffers();
        }

        application.shutdown();
    }

    /**
     * GLFW getter for Time()
     * @return {@link GLFW#glfwGetTime()}
     */
    private static double glfwTimeSeconds() {
        return GLFW.glfwGetTime();
    }
}
