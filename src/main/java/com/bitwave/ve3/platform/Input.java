package com.bitwave.ve3.platform;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Main handling class for client input, translated into GLFW calls.
 */
public class Input {
    private final boolean[] keysDown = new boolean[GLFW_KEY_LAST + 1];
    private final boolean[] keysDownPrev = new boolean[GLFW_KEY_LAST + 1];

    private final boolean[] mouseDown = new boolean[GLFW_MOUSE_BUTTON_LAST + 1];
    private final boolean[] mouseDownPrev = new boolean[GLFW_MOUSE_BUTTON_LAST + 1];

    private double mouseX, mouseY;
    private double mouseDeltaX, mouseDeltaY;
    private double lastMouseX, lastMouseY;
    private boolean firstMouseEvent = true;

    private double scrollDeltaX, scrollDeltaY;

    void attachCallbacks(long windowHandle) {
        glfwSetKeyCallback(windowHandle, (win, key, scancode, action, mods) -> {
            if (key < 0 || key > GLFW_KEY_LAST) return;
            if (action == GLFW_PRESS) keysDown[key] = true;
            else if (action == GLFW_RELEASE) keysDown[key] = false;
        });

        glfwSetMouseButtonCallback(windowHandle, (win, button, action, mods) -> {
            if (button < 0 || button > GLFW_MOUSE_BUTTON_LAST) return;
            if (action == GLFW_PRESS) mouseDown[button] = true;
            else if (action == GLFW_RELEASE) mouseDown[button] = false;
        });

        glfwSetCursorPosCallback(windowHandle, (win, xPos, yPos) -> {
            mouseX = xPos;
            mouseY = yPos;
        });

        glfwSetScrollCallback(windowHandle, (win, xOffset, yOffset) -> {
            scrollDeltaX += xOffset;
            scrollDeltaY += yOffset;
        });
    }

    void beginFrame() {
        System.arraycopy(keysDown, 0, keysDownPrev, 0, keysDown.length);
        System.arraycopy(mouseDown, 0, mouseDownPrev, 0, mouseDown.length);

        if (firstMouseEvent) {
            lastMouseX = mouseX;
            lastMouseY = mouseY;
            firstMouseEvent = false;
        }
        mouseDeltaX = mouseX - lastMouseX;
        mouseDeltaY = mouseY - lastMouseY;
        lastMouseX = mouseX;
        lastMouseY = mouseY;

        scrollDeltaX = 0;
        scrollDeltaY = 0;
    }

    // Getters for key and mouse activity

    public boolean isKeyDown(int key) { return keysDown[key]; }
    public boolean isKeyPressed(int key) { return keysDown[key] && !keysDownPrev[key]; }
    public boolean isKeyReleased(int key) { return !keysDown[key] && keysDownPrev[key]; }

    public boolean isMouseButtonDown(int button) { return mouseDown[button]; }
    public boolean isMouseButtonPressed(int button) { return mouseDown[button] && !mouseDownPrev[button]; }
    public boolean isMouseButtonReleased(int button) { return !mouseDown[button] && mouseDownPrev[button]; }

    public double mouseX() {  return mouseX; }
    public double mouseY() { return mouseY; }
    public double mouseDeltaX() { return mouseDeltaX; }
    public double mouseDeltaY() { return mouseDeltaY; }
    public double scrollDeltaX() { return scrollDeltaX; }
    public double scrollDeltaY() { return scrollDeltaY; }
}
