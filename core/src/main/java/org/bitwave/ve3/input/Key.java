package org.bitwave.ve3.input;

/**
 * Platform neutral keyboard keys used by engine gameplay code.
 *
 * <p>Platform backends translate these values into their native key codes.
 * Keeping gameplay code independent of GLFW makes future input backends easier.</p>
 */
public enum Key {
    W,
    A,
    S,
    D,
    SPACE,
    LEFT_SHIFT,
    ESCAPE
}
