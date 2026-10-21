package org.bitwave.ve3desktop;

import org.bitwave.facet.Camera;
import org.bitwave.ve3.UpdateContext;
import org.bitwave.ve3.input.Key;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

/**
 * Desktop implementation of the game loop.
 *
 * <p>This class is responsible for managing the update/render loop, polling input events and managing the window lifecycle.</p>
 */
public final class Lwjgl3GameLoop implements org.bitwave.ve3.loop.GameLoop {
    private final Lwjgl3Input input = new Lwjgl3Input();
    private final Camera camera = new Camera();
    private final Window window;
    private double previousTime;
    private long frameNumber;

    public Lwjgl3GameLoop(Window window) {
        this.window = window;
    }

    @Override
    public void init() {
        input.attach(window.handle());
        previousTime = glfwTime();
        GL11.glClearColor(0.08f, 0.10f, 0.14f, 1.0f);
    }

    @Override
    public boolean shouldContinue() {
        return !window.shouldClose();
    }

    @Override
    public void update(UpdateContext context) {
        window.pollEvents();

        float speed = 4.0f;
        float distance = speed * (float) context.deltaTime();

        if (input.isKeyDown(Key.W)) camera.moveRelative(distance, 0.0f);
        if (input.isKeyDown(Key.S)) camera.moveRelative(-distance, 0.0f);
        if (input.isKeyDown(Key.A)) camera.moveRelative(0.0f, -distance);
        if (input.isKeyDown(Key.D)) camera.moveRelative(0.0f, distance);
        if (input.isKeyDown(Key.SPACE)) camera.moveWorld(0.0f, distance, 0.0f);
        if (input.isKeyDown(Key.LEFT_SHIFT)) camera.moveWorld(0.0f, -distance, 0.0f);
        if (input.wasKeyPressed(Key.ESCAPE)) window.requestClose();

        input.endFrame();
    }

    @Override
    public void render() {
        GL11.glClear(
                GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT
        );
    }

    @Override
    public double pollDeltaTime() {
        double now = glfwTime();
        double delta = now - previousTime;
        previousTime = now;

        return Math.max(0.0, delta);
    }

    @Override
    public void shutdown() {
        try {
            window.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static double glfwTime() {
        return GLFW.glfwGetTime();
    }
}
