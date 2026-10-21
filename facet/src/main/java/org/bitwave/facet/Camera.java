package org.bitwave.facet;

import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * A simple first-person perspective camera.
 *
 * <p>Yaw rotates around the vertical Y axis; pitch rotates around the camera's local X axis.
 * Camera produces a view-projection matrix suitable for uploading to a shader.</p>
 */
public final class Camera {
    private final Vector3f position = new Vector3f();
    private final Vector3f forward = new Vector3f(0.0f, 0.0f, -1.0f);
    private final Vector3f right = new Vector3f(1.0f, 0.0f, 0.0f);
    private final Vector3f up = new Vector3f(0.0f, 1.0f, 0.0f);

    private float yaw;
    private float pitch;

    private final Matrix4f viewProjection = new Matrix4f();

    public Camera() {
        updateDirection();
    }

    public void moveWorld(float x, float y, float z) {
        position.add(x, y, z);
    }

    public void moveRelative(float forwardAmount, float rightAmount) {
        position.add(
                forward.x * forwardAmount + right.x * rightAmount,
                forward.y * forwardAmount + right.y * rightAmount,
                forward.z * forwardAmount + right.z * rightAmount
        );
    }

    public void addYaw(float yawDelta) {
        yaw += yawDelta;
        updateDirection();
    }

    public void addPitch(float pitchDelta) {
        pitch = Math.clamp(pitch + pitchDelta, -89.0f, 89.0f);
        updateDirection();
    }

    public Vector3f position() {
        return new Vector3f(position);
    }

    public Matrix4f viewProjection(
            float fovRadians,
            float aspectRatio,
            float nearPlane,
            float farPlane
    ) {
        Vector3f target = new Vector3f(position).add(forward);

        return viewProjection
                .identity()
                .perspective(fovRadians, aspectRatio, nearPlane, farPlane)
                .lookAt(position, target, up);
    }

    private void updateDirection() {
        float cosPitch = (float) Math.cos(pitch);

        forward.set(
                (float) Math.cos(yaw) * cosPitch,
                (float) Math.sin(pitch),
                (float) Math.sin(yaw) * cosPitch
        ).normalize();

        right.set(forward).cross(up).normalize();
    }
}
