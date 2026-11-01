package org.bitwave.ve3.core.input;

/**
 * Interface for an input system that handles keyboard and mouse input.
 * @author k4i
 */
public interface InputSystem {
    void poll();

    boolean isKeyDown(int key);
    boolean isKeyPressed(int key);
    boolean isKeyReleased(int key);

    boolean isMouseButtonDown(int button);
    boolean isMouseButtonPressed(int button);

    double mouseDeltaX();
    double mouseDeltaY();
}
