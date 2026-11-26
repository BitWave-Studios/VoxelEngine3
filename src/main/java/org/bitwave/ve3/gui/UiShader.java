package org.bitwave.ve3.gui;

import org.bitwave.ve3.core.render.ShaderProgram;
import org.joml.Matrix4f;

import java.util.List;

import static org.lwjgl.opengl.GL20.*;

/**
 * A shader program specifically designed for rendering UI elements.
 */
public final class UiShader {
    private final ShaderProgram shaderProgram;
    private final int projectionLocation;
    private final int textureLocation;

    public UiShader(String vertexShaderPath, String fragmentShaderPath) {
        shaderProgram = new ShaderProgram(List.of(
                new ShaderProgram.ShaderModuleData(vertexShaderPath, GL_VERTEX_SHADER),
                new ShaderProgram.ShaderModuleData(fragmentShaderPath, GL_FRAGMENT_SHADER)
        ));

        projectionLocation = glGetUniformLocation(shaderProgram.getProgramId(), "uProjection");

        textureLocation = glGetUniformLocation(shaderProgram.getProgramId(), "uTexture");

        glActiveTexture(GL_TEXTURE0);
        bind();
        setInt("uTexture", 0);
        unbind();
    }

    public void bind() {
        shaderProgram.bind();
    }

    public void unbind() {
        shaderProgram.unbind();
    }

    public void setProjection(float width, float height) {
        Matrix4f projection = new Matrix4f().ortho2D(0f, width, height, 0f);

        glUniformMatrix4fv(projectionLocation, false, projection.get(new float[16]));
    }

    public void setInt(String name, int value) {
        glUniform1i(glGetUniformLocation(shaderProgram.getProgramId(), name), value);
    }

    public void cleanup() {
        shaderProgram.cleanup();
    }
}
