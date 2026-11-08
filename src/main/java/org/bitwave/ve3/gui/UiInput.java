package org.bitwave.ve3.gui;

public final class UiInput {
    public float mouseX;
    public float mouseY;

    public float scrollX;
    public float scrollY;

    public final boolean[] previousMouseDown = new boolean[3];
    public final boolean[] currentMouseDown = new boolean[3];

    public final boolean[] previousKeyDown = new boolean[512];
    public final boolean[] currentKeyDown = new boolean[512];

    public final StringBuilder typedCharacters = new StringBuilder();

    public void beginFrame() {
        scrollX = 0;
        scrollY = 0;
        typedCharacters.setLength(0);
    }

    public void endFrame() {
        for (int i = 0; i < previousMouseDown.length; i++) {
            previousMouseDown[i] = currentMouseDown[i];
        }

        for (int i = 0; i < previousKeyDown.length; i++) {
            previousKeyDown[i] = currentKeyDown[i];
        }
    }

    public void setMousePosition(float x, float y) {
        mouseX = x;
        mouseY = y;
    }

    public void setMouseButton(int button, boolean down) {
        if (button >= 0 && button < currentMouseDown.length) {
            currentMouseDown[button] = down;
        }
    }

    public void setKey(int key, boolean down) {
        if (key >= 0 && key < currentKeyDown.length) {
            currentKeyDown[key] = down;
        }
    }

    public void typedCharacter(char character) {
        typedCharacters.append(character);
    }

    public void addScroll(float x, float y) {
        scrollX += x;
        scrollY += y;
    }

    public boolean isMouseDown(int button) {
        return currentMouseDown[button];
    }

    public boolean wasMousePressed(int button) {
        return currentMouseDown[button] && !previousMouseDown[button];
    }

    public boolean wasMouseReleased(int button) {
        return !currentMouseDown[button] && previousMouseDown[button];
    }

    public boolean isKeyDown(int key) {
        return currentKeyDown[key];
    }

    public boolean wasKeyPressed(int key) {
        return currentKeyDown[key] && !previousKeyDown[key];
    }

    public String consumeTypedCharacters() {
        String text = typedCharacters.toString();
        typedCharacters.setLength(0);
        return text;
    }
}
