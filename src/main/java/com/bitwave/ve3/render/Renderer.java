package com.bitwave.ve3.render;

import static org.lwjgl.opengl.GL11.*;

public class Renderer {
    private final ResourceRegistry<Mesh> meshes = new ResourceRegistry<>();
    private final ResourceRegistry<Shader> shaders = new ResourceRegistry<>();
    private final ResourceRegistry<Material> materials = new ResourceRegistry<>();

    public MeshHandle registerMesh(Mesh mesh) {
        return new MeshHandle(meshes.register(mesh));
    }

    public ShaderHandle registerShader(Shader shader) {
        return new ShaderHandle(shaders.register(shader));
    }

    public MaterialHandle registerMaterial(Material material) {
        return new MaterialHandle(materials.register(material));
    }

    public void render(RenderSnapshot snapshot) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glEnable(GL_DEPTH_TEST);

        for (RenderSnapshot.RenderInstance instance : snapshot.instances()) {
            Mesh mesh = meshes.get(instance.mesh().id());
            Material material = materials.get(instance.material().id());
            Shader shader = shaders.get(material.shader().id());

            shader.bind();
            shader.setUniform("uModel", instance.modelMatrix());
            shader.setUniform("uView", snapshot.viewMatrix());
            shader.setUniform("uProjection", snapshot.projectionMatrix());
            shader.setUniform("uColor", material.r(), material.g(), material.b());

            mesh.bind();
            mesh.draw();
        }
    }
}
