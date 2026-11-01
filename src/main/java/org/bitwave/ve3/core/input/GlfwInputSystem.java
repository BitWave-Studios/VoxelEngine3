package org.bitwave.ve3.core.input;

import org.bitwave.ve3.core.window.Window;
import org.lwjgl.glfw.GLFW;

/**
 * Implementation of the InputSystem interface using GLFW.
 * @apiNote Replaces old/template input system that was handled through {@link org.bitwave.ve3.core.player.Player}.
 * @author k4i
 */
public class GlfwInputSystem implements InputSystem {
    private final Window window;

    private final boolean[] keyDown = new boolean[512];
    private final boolean[] keyPressed = new boolean[512];
    private final boolean[] keyReleased = new boolean[512];

    private final boolean[] mouseDown = new boolean[8];
    private final boolean[] mousePressed = new boolean[8];
    private final boolean[] mouseReleased = new boolean[8];

    private double mouseDeltaX;
    private double mouseDeltaY;
    private double lastMouseX;
    private double lastMouseY;

    public GlfwInputSystem(Window window) {
        this.window = window;
    }

    @Override
    public void poll() {
        long handle = window.getWindowHandle();

        for (int i = 0; i < 512; i++) {
            boolean current = GLFW.glfwGetKey(handle, i) == GLFW.GLFW_PRESS;
            keyPressed[i] = current && !keyDown[i];
            keyReleased[i] = !current && keyDown[i];
            keyDown[i] = current;
        }

        for (int i = 0; i < 8; i++) {
            boolean current = GLFW.glfwGetMouseButton(handle, i) == GLFW.GLFW_PRESS;
            mousePressed[i] = current && !mouseDown[i];
            mouseReleased[i] = !current && mouseDown[i];
            mouseDown[i] = current;
        }

        double[] x = new double[1];
        double[] y = new double[1];
        GLFW.glfwGetCursorPos(handle, x, y);

        mouseDeltaX = x[0] - lastMouseX;
        mouseDeltaY = y[0] - lastMouseY;

        lastMouseX = x[0];
        lastMouseY = y[0];
    }

    @Override
    public boolean isKeyDown(int key) {
        return keyDown[key];
    }

    @Override
    public boolean isKeyPressed(int key) {
        return keyPressed[key];
    }

    @Override
    public boolean isKeyReleased(int key) {
        return keyReleased[key];
    }

    @Override
    public boolean isMouseButtonDown(int button) {
        return mouseDown[button];
    }

    @Override
    public boolean isMouseButtonPressed(int button) {
        return mousePressed[button];
    }

    @Override
    public double mouseDeltaX() {
        return mouseDeltaX;
    }

    @Override
    public double mouseDeltaY() {
        return mouseDeltaY;
    }
}
