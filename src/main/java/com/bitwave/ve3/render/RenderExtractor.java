package com.bitwave.ve3.render;

import com.bitwave.ve3.core.ecs.World;
import com.bitwave.ve3.core.ecs.component.ActiveCamera;
import com.bitwave.ve3.core.ecs.component.Camera;
import com.bitwave.ve3.core.ecs.component.MeshRenderer;
import com.bitwave.ve3.core.ecs.component.Transform;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import java.util.ArrayList;
import java.util.List;

public class RenderExtractor {
    public RenderSnapshot extract(World world, float aspectRatio) {
        List<RenderSnapshot.RenderInstance> instances = new ArrayList<>();

        world.query(Transform.class, MeshRenderer.class).forEach((entity, transform, meshRenderer) -> {
            Matrix4f model = new Matrix4f()
                    .translate(transform.position())
                    .rotate(transform.rotation())
                    .scale(transform.scale());

            instances.add(new RenderSnapshot.RenderInstance(
                    meshRenderer.mesh(),
                    meshRenderer.material(),
                    model
            ));
        });

        CameraData cameraData = findActiveCamera(world, aspectRatio);

        return new RenderSnapshot(instances, cameraData.view(), cameraData.projection());
    }

    private CameraData findActiveCamera(World world, float aspectRatio) {
        CameraData[] result = new CameraData[1];

        world.query(Transform.class, Camera.class).forEach((entity, transform, camera) -> {
            if (world.has(entity, ActiveCamera.class)) {
                Matrix4f view = new Matrix4f()
                        .rotate(transform.rotation().conjugate(new Quaternionf()))
                        .translate(-transform.position().x(), -transform.position().y(), -transform.position().z());

                Matrix4f projection = new Matrix4f().perspective(
                        (float) Math.toRadians(camera.fovDegrees()),
                        aspectRatio,
                        camera.nearPlane(),
                        camera.farPlane()
                );

                result[0] = new CameraData(view, projection);
            }
        });

        if (result[0] == null) {
            result[0] = new CameraData(new Matrix4f(), new Matrix4f().perspective(
                    (float) Math.toRadians(70f), aspectRatio, 0.1f, 1000f));
        }

        return result[0];
    }

    private record CameraData(Matrix4f view, Matrix4f projection) {}
}
