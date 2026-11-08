package org.bitwave.ve3.core.input;

import org.bitwave.ve3.gui.UiInput;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

public class MouseInput {
    private Vector2f currentPos;
    private Vector2f displVec;
    private Vector2f previousPos;

    private boolean inWindow;
    private boolean leftButtonPressed;
    private boolean rightButtonPressed;

    private UiInput uiInput;

    public MouseInput(long windowHandle) {
        previousPos = new Vector2f(-1, -1);
        currentPos = new Vector2f();
        displVec = new Vector2f();

        leftButtonPressed = false;
        rightButtonPressed = false;
        inWindow = false;

        glfwSetCursorPosCallback(windowHandle, (handle, xpos, ypos) -> {
            currentPos.x = (float) xpos;
            currentPos.y = (float) ypos;

            if (uiInput != null) {
                uiInput.setMousePosition((float) xpos, (float) ypos);
            }
        });

        glfwSetCursorEnterCallback(windowHandle, (handle, entered) ->
                inWindow = entered
        );

        glfwSetMouseButtonCallback(windowHandle, (handle, button, action, mods) -> {
            boolean pressed = action == GLFW_PRESS;

            leftButtonPressed = button == GLFW_MOUSE_BUTTON_1 && pressed;
            rightButtonPressed = button == GLFW_MOUSE_BUTTON_2 && pressed;

            if (uiInput != null) {
                uiInput.setMouseButton(button, pressed);
            }
        });
    }

    public void setUiInput(UiInput uiInput) {
        this.uiInput = uiInput;
    }

    public Vector2f getCurrentPos() {
        return currentPos;
    }

    public Vector2f getDisplVec() {
        return displVec;
    }

    public void input() {
        displVec.x = 0;
        displVec.y = 0;

        if (previousPos.x > 0 && previousPos.y > 0 && inWindow) {
            double deltaX = currentPos.x - previousPos.x;
            double deltaY = currentPos.y - previousPos.y;

            if (deltaX != 0) {
                displVec.y = (float) deltaX;
            }

            if (deltaY != 0) {
                displVec.x = (float) deltaY;
            }
        }

        previousPos.x = currentPos.x;
        previousPos.y = currentPos.y;
    }

    public boolean isLeftButtonPressed() {
        return leftButtonPressed;
    }

    public boolean isRightButtonPressed() {
        return rightButtonPressed;
    }
}
