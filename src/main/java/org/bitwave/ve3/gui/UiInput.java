package org.bitwave.ve3.gui;

public final class UiInput {
    public static final int MOUSE_BUTTON_COUNT = 8;
    public static final int KEY_COUNT = 512;

    public float mouseX;
    public float mouseY;
    public float mouseDX;
    public float mouseDY;

    public float scrollX;
    public float scrollY;

    public int modifiers;

    public final boolean[] previousMouseDown = new boolean[MOUSE_BUTTON_COUNT];
    public final boolean[] currentMouseDown = new boolean[MOUSE_BUTTON_COUNT];

    public final boolean[] previousKeyDown = new boolean[KEY_COUNT];
    public final boolean[] currentKeyDown = new boolean[KEY_COUNT];

    public final StringBuilder typedCharacters = new StringBuilder();

    public void beginFrame(float newMouseX, float newMouseY, float newScrollX, float newScrollY, int newModifiers, String newTypedCharacters) {
        mouseDX = newMouseX - mouseX;
        mouseDY = newMouseY - mouseY;

        mouseX = newMouseX;
        mouseY = newMouseY;

        scrollX = newScrollX;
        scrollY = newScrollY;

        modifiers = newModifiers;

        typedCharacters.setLength(0);
        typedCharacters.append(newTypedCharacters);
    }

    public void endFrame() {
        System.arraycopy(
                currentMouseDown, 0,
                previousMouseDown, 0,
                MOUSE_BUTTON_COUNT
        );

        System.arraycopy(
                currentKeyDown, 0,
                previousKeyDown, 0,
                KEY_COUNT
        );
    }

    public void setMousePosition(float x, float y) {
        mouseX = x;
        mouseY = y;
    }

    public void setMouseButton(int button, boolean down) {
        if (isValidMouseButton(button)) {
            currentMouseDown[button] = down;
        }
    }

    public void setKey(int key, boolean down) {
        if (isValidKey(key)) {
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
        requireValidMouseButton(button);
        return currentMouseDown[button];
    }

    public boolean wasMousePressed(int button) {
        requireValidMouseButton(button);
        return currentMouseDown[button] && !previousMouseDown[button];
    }

    public boolean wasMouseReleased(int button) {
        requireValidMouseButton(button);
        return !currentMouseDown[button] && previousMouseDown[button];
    }

    public boolean isKeyDown(int key) {
        requireValidKey(key);
        return currentKeyDown[key];
    }

    public boolean wasKeyPressed(int key) {
        requireValidKey(key);
        return currentKeyDown[key] && !previousKeyDown[key];
    }

    public boolean wasKeyReleased(int key) {
        requireValidKey(key);
        return !currentKeyDown[key] && previousKeyDown[key];
    }

    public String consumeTypedCharacters() {
        String text = typedCharacters.toString();
        typedCharacters.setLength(0);
        return text;
    }

    private static boolean isValidMouseButton(int button) {
        return button >= 0 && button < MOUSE_BUTTON_COUNT;
    }

    private static boolean isValidKey(int key) {
        return key >= 0 && key < KEY_COUNT;
    }

    private static void requireValidMouseButton(int button) {
        if (!isValidMouseButton(button)) {
            throw new IndexOutOfBoundsException(
                    "Mouse button out of range: " + button
            );
        }
    }

    private static void requireValidKey(int key) {
        if (!isValidKey(key)) {
            throw new IndexOutOfBoundsException("Key out of range: " + key);
        }
    }
}
