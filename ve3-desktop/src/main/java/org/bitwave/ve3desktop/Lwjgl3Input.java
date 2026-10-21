package org.bitwave.ve3desktop;

import org.bitwave.ve3.input.Input;
import org.bitwave.ve3.input.Key;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import static org.lwjgl.glfw.GLFW.*;

public final class Lwjgl3Input implements Input {
    private final Map<Key, Boolean> heldKeys = new EnumMap<>(Key.class);
    private final Set<Key> pressedThisFrame = EnumSet.noneOf(Key.class);
    private final Set<Key> releasedThisFrame = EnumSet.noneOf(Key.class);

    public Lwjgl3Input() {
        for (Key key : Key.values()) {
            heldKeys.put(key, false);
        }
    }

    public void attach(long windowHandle) {
        glfwSetKeyCallback(windowHandle, (window, glfwKey, scancode, action, mods) -> {
            Key key = mapKey(glfwKey);

            if (key == null) {
                return;
            }

            switch (action) {
                case GLFW_PRESS -> {
                    heldKeys.put(key, true);
                    pressedThisFrame.add(key);
                }
                case GLFW_RELEASE -> {
                    heldKeys.put(key, false);
                    releasedThisFrame.add(key);
                }
                default -> {
                    // GLFW_REPEAT is deliberately ignored, as it does not represent a new key press or release event.
                }
            }
        });
    }

    public void endFrame() {
        pressedThisFrame.clear();
        releasedThisFrame.clear();
    }

    @Override
    public boolean isKeyDown(Key key) {
        return heldKeys.getOrDefault(key, false);
    }

    @Override
    public boolean wasKeyPressed(Key key) {
        return pressedThisFrame.contains(key);
    }

    @Override
    public boolean wasKeyReleased(Key key) {
        return releasedThisFrame.contains(key);
    }

    private static Key mapKey(int glfwKey) {
        return switch (glfwKey) {
            case GLFW_KEY_W -> Key.W;
            case GLFW_KEY_A -> Key.A;
            case GLFW_KEY_S -> Key.S;
            case GLFW_KEY_D -> Key.D;
            case GLFW_KEY_SPACE -> Key.SPACE;
            case GLFW_KEY_LEFT_SHIFT -> Key.LEFT_SHIFT;
            case GLFW_KEY_ESCAPE -> Key.ESCAPE;
            default -> null; // Unmapped key
        };
    }
}
