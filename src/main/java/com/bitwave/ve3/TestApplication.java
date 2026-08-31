package com.bitwave.ve3;

import com.bitwave.ve3.core.ecs.*;
import com.bitwave.ve3.core.ecs.component.ActiveCamera;
import com.bitwave.ve3.core.ecs.component.Camera;
import com.bitwave.ve3.core.ecs.component.MeshRenderer;
import com.bitwave.ve3.core.ecs.component.Transform;
import com.bitwave.ve3.platform.Application;
import com.bitwave.ve3.platform.Window;
import com.bitwave.ve3.render.*;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL11.*;

/**
 * Testing class for running an Application with the VE3 platform.
 */
public class TestApplication implements Application {
    private World world;
    private Scheduler scheduler;
    private Renderer renderer;
    private RenderExtractor extractor;
    private Window window;

    private static final String VERTEX_SRC = """
            #version 330 core
            layout(location = 0) in vec3 aPos;
            uniform mat4 uModel;
            uniform mat4 uView;
            uniform mat4 uProjection;
            void main() {
                gl_Position = uProjection * uView * uModel * vec4(aPos, 1.0);
            }
            """;

    private static final String FRAGMENT_SRC = """
            #version 330 core
            out vec4 FragColor;
            uniform vec3 uColor;
            void main() {
                FragColor = vec4(uColor, 1.0);
            }
            """;

    @Override
    public void init(Window window) {
        this.window = window;
        world = new World();
        scheduler = new Scheduler();
        renderer = new Renderer();
        extractor = new RenderExtractor();

        Shader shader = new Shader(VERTEX_SRC, FRAGMENT_SRC);
        ShaderHandle shaderHandle = renderer.registerShader(shader);

        Material material = new Material(shaderHandle, 0.9f, 0.4f, 0.2f);
        MaterialHandle materialHandle = renderer.registerMaterial(material);

        float[] triangleVerts = {
                0.0f, 0.5f, 0.0f,
               -0.5f, -0.5f, 0.0f,
                0.5f, -0.5f, 0.0f,
        };
        Mesh mesh = new Mesh(triangleVerts);
        MeshHandle meshHandle = renderer.registerMesh(mesh);

        Entity triangle = world.spawn();
        world.add(triangle, Transform.identity());
        world.add(triangle, new MeshRenderer(meshHandle, materialHandle));

        Entity camera = world.spawn();
        world.add(camera, new Transform(
                new Vector3f(0, 0, 3),
                new Quaternionf(),
                new Vector3f(1, 1, 1)
        ));
        world.add(camera, Camera.perspectiveDefault());
        world.add(camera, new ActiveCamera());
    }

    @Override
    public void fixedUpdate(float fixedDelta) {
        scheduler.tick(world, fixedDelta);
    }

    @Override
    public void render(float alpha) {
        float aspectRatio = (float) window.width() / (float) window.height();
        RenderSnapshot snapshot = extractor.extract(world, aspectRatio);
        renderer.render(snapshot);
    }

    @Override
    public void shutdown() {
        // TODO: Resource cleanup comes after registries support iteration/disposal (later)
    }
}
