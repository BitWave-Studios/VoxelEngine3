package org.bitwave.ve3.core.player;

import org.bitwave.ve3.core.window.Window;
import org.bitwave.ve3.core.world.World;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

/**
 * Basic Player controller with movement and simple gravity.
 */
public class Player {
    private final Camera camera;
    private final Window window;

    private final Vector3f position = new Vector3f();
    private float pitch = 0f;
    private float yaw = -90f;

    private float speed = 5f;
    private float mouseSensitivity = 0.1f;

    private double lastMouseX, lastMouseY;
    private boolean firstMouse = true;

    // Track whether cursor is currently captured for mouseLook
    private boolean cursorCaptured = true;

    private float verticalSpeed = 0f;
    private final float gravity = -20f;

    public Player(float x, float y, float z, Window window) {
        this.window = window;
        this.position.set(x, y, z);

        // Use window aspect ratio instead of hardcoded values
        int fbW = window.getFramebufferWidth();
        int fbH = window.getFramebufferHeight();
        float aspect = fbW > 0 && fbH > 0
                ? (float) fbW / (float) fbH
                : (float) window.getWidth() / (float) window.getHeight();

        Camera camera = new Camera(70f, aspect, 0.01f, 100f);
        camera.setPosition(x, y, z);
        this.camera = camera;
    }

    public void update(float dt, World world) {
        long win = window.getWindowHandle();

        // Toggle cursor mode with ESC
        if (GLFW.glfwGetKey(win, GLFW.GLFW_KEY_ESCAPE) == GLFW.GLFW_PRESS) {
            if (cursorCaptured) {
                GLFW.glfwSetInputMode(win, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
                cursorCaptured = false;
                firstMouse = true;
            }
        }

        // If left mouse button pressed while cursor is free, recapture
        if (!cursorCaptured && GLFW.glfwGetMouseButton(win, GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS) {
            GLFW.glfwSetInputMode(win, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
            GLFW.glfwSetCursorPos(win, window.getFramebufferWidth() / 2.0, window.getFramebufferHeight() / 2.0);
            cursorCaptured = true;
            firstMouse = true;
        }

        // Mouse look only when cursor is captured
        if (cursorCaptured) {
            double[] mouseX = new double[1], mouseY = new double[1];
            GLFW.glfwGetCursorPos(win, mouseX, mouseY);

            if (firstMouse) {
                double centerX = window.getFramebufferWidth() / 2.0;
                double centerY = window.getFramebufferHeight() / 2.0;
                GLFW.glfwSetCursorPos(win, centerX, centerY);
                lastMouseX = centerX;
                lastMouseY = centerY;
                firstMouse = false;
            }

            double dx = mouseX[0] - lastMouseX;
            double dy = lastMouseY - mouseY[0];
            lastMouseX = mouseX[0];
            lastMouseY = mouseY[0];

            yaw += (float) (dx * mouseSensitivity);
            pitch += (float) (dy * mouseSensitivity);
            if (pitch > 89f) pitch = 89f;
            if (pitch < -89f) pitch = -89f;

            camera.setYaw(yaw);
            camera.setPitch(pitch);
        }

        // Movement
        Vector3f direction = new Vector3f();
        if (GLFW.glfwGetKey(win, GLFW.GLFW_KEY_W) == GLFW.GLFW_PRESS) direction.z -= 1;
        if (GLFW.glfwGetKey(win, GLFW.GLFW_KEY_S) == GLFW.GLFW_PRESS) direction.z += 1;
        if (GLFW.glfwGetKey(win, GLFW.GLFW_KEY_A) == GLFW.GLFW_PRESS) direction.x -= 1;
        if (GLFW.glfwGetKey(win, GLFW.GLFW_KEY_D) == GLFW.GLFW_PRESS) direction.x += 1;

        if (direction.lengthSquared() > 0) direction.normalize();

        // Convert local direction to world space
        float rad = (float) Math.toRadians(yaw);
        Vector3f forward = new Vector3f((float)Math.cos(rad), 0, (float)Math.sin(rad));
        Vector3f right = new Vector3f(-forward.z, 0, forward.x);

        Vector3f move = new Vector3f();
        move.fma(direction.z, forward, move);
        move.fma(direction.x, right, move);
        move.mul(speed * dt);

        // Horizontal movement with basic collision
        tryMove(position.x + move.x, position.y, position.z + move.z, world);

        // Gravity
        verticalSpeed += gravity * dt;
        tryMove(position.x, position.y + verticalSpeed * dt, position.z, world);

        // Update camera position
        camera.setPosition(position.x, position.y + 0.6f, position.z);

        // Update camera aspect if FrameBuffer changed
        int fbW = window.getFramebufferWidth();
        int fbH = window.getFramebufferHeight();
        if (fbW <= 0 || fbH <= 0) return;
        float newAspect = (float) fbW / (float) fbH;
        camera.setAspect(newAspect);
    }

    private void tryMove(float nx, float ny, float nz, World world) {
        // AABB Player Size
        float playerHalf = 0.2f;
        int minX = (int) Math.floor(nx - playerHalf);
        int maxX = (int) Math.floor(nx + playerHalf);
        int minY = (int) Math.floor(ny - 0.0f);
        int maxY = (int) Math.floor(ny + 1.6f);
        int minZ = (int) Math.floor(nz - playerHalf);
        int maxZ = (int) Math.floor(nz + playerHalf);

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    if (world.isSolid(x, y, z)) {
                        // Simple collision -> cancel movement
                        return;
                    }
                }
            }
        }

        // If no collision, accept
        position.set(nx, ny, nz);
    }

    public Camera getCamera() { return camera; }

    public void setYaw(float yaw) {
        this.yaw = yaw;
        camera.setYaw(yaw);
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
        camera.setPitch(pitch);
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }
}
