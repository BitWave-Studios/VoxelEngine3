package org.bitwave.ve3.core.player;

import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * A simple camera
 */
public class Camera {
    private final Vector3f position = new Vector3f();
    private float pitch = 0f; // Rotation X
    private float yaw = -90f; // Rotation Y

    private final Matrix4f projection = new Matrix4f();
    private final Matrix4f view = new Matrix4f();

    private final float fov;
    private float aspect;
    private final float near;
    private final float far;

    public Camera(float fov, float aspect, float near, float far) {
        this.fov = fov;
        this.aspect = aspect;
        this.near = near;
        this.far = far;
        updateProjection();
    }

    private void updateProjection() {
        projection.identity().perspective((float) Math.toRadians(fov), aspect, near, far);
    }

    // Allow changing aspect ratio when FrameBuffer size changes
    public void setAspect(float aspect) {
        if (this.aspect != aspect) {
            this.aspect = aspect;
            updateProjection();
        }
    }

    public void setPosition(float x, float y, float z) { position.set(x, y, z); }
    public Vector3f getPosition() { return position; }

    public void setPitch(float pitch) { this.pitch = pitch; }
    public void setYaw(float yaw) { this.yaw = yaw; }

    public Matrix4f getProjection() { return projection; }

    public Matrix4f getView() {
        Vector3f front = new Vector3f();
        front.x = (float) (Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
        front.y = (float) Math.sin(Math.toRadians(pitch));
        front.z = (float) (Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));

        Vector3f center = new Vector3f(position).add(front);
        view.identity().lookAt(position, center, new Vector3f(0,1,0));
        return view;
    }
}
