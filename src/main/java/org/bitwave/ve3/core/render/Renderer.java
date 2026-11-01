package org.bitwave.ve3.core.render;

import org.bitwave.ve3.core.player.Camera;
import org.bitwave.ve3.core.world.World;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

/**
 * Small renderer that draws coloured cubes.
 */
public class Renderer {
    private Shader shader;
    private int vao;
    private int vbo;
    private int ebo;

    // Cached uniform locations
    private int projLoc;
    private int viewLoc;
    private int modelLoc;
    private int colorLoc;

    // Reusable matrix buffer
    private final FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

    private static final float[] CUBE_VERTICES = new float[]{
            // Pos (x,y,z)
            -0.5f, -0.5f, 0.5f,
            0.5f, -0.5f, 0.5f,
            0.5f,  0.5f, 0.5f,
            -0.5f,  0.5f, 0.5f,
            -0.5f, -0.5f, -0.5f,
            0.5f, -0.5f, -0.5f,
            0.5f,  0.5f, -0.5f,
            -0.5f,  0.5f, -0.5f
    };

    private static final int[] INDICES = new int[]{
            0,1,2, 2,3,0, // Front
            1,5,6, 6,2,1, // Right
            5,4,7, 7,6,5, // Back
            4,0,3, 3,7,4, // Left
            3,2,6, 6,7,3, // Top
            4,5,1, 1,0,4  // Bottom
    };

    public void init() {
        String vs = "#version 330 core\n" +
                "layout(location=0) in vec3 aPos;\n" +
                "uniform mat4 projection;\n" +
                "uniform mat4 view;\n" +
                "uniform mat4 model;\n" +
                "void main() { gl_Position = projection * view * model * vec4(aPos, 1.0); }";

        String fs = "#version 330 core\n" +
                "out vec4 FragColor;\n" +
                "uniform vec3 color;\n" +
                "void main() { FragColor = vec4(color, 1.0); }";

        shader = new Shader(vs, fs);

        // Create VAO/VBO/EBO using direct buffers
        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        FloatBuffer vb = BufferUtils.createFloatBuffer(CUBE_VERTICES.length);
        vb.put(CUBE_VERTICES).flip();
        glBufferData(GL_ARRAY_BUFFER, vb, GL_STATIC_DRAW);

        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * Float.BYTES, 0);

        // index buffer
        ebo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        IntBuffer ib = BufferUtils.createIntBuffer(INDICES.length);
        ib.put(INDICES).flip();
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, ib, GL_STATIC_DRAW);

        glBindVertexArray(0);

        // Enable depth test and back-face culling
        glEnable(GL_DEPTH_TEST);
        glDisable(GL_CULL_FACE);

        // Cache uniform locations (once)
        shader.bind();
        projLoc = glGetUniformLocation(shader.getId(), "projection");
        viewLoc = glGetUniformLocation(shader.getId(), "view");
        modelLoc = glGetUniformLocation(shader.getId(), "model");
        colorLoc = glGetUniformLocation(shader.getId(), "color");
        shader.unbind();

        checkGlError("Renderer.init");
    }

    public void render(World world, Camera camera) {
        glClearColor(0.53f, 0.81f, 0.92f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        shader.bind();

        Matrix4f projection = camera.getProjection();
        Matrix4f view = camera.getView();
        Matrix4f model = new Matrix4f().translate(0f, 0f, 0f);

        glUniformMatrix4fv(projLoc, false, toFloatBuffer(projection));
        glUniformMatrix4fv(viewLoc, false, toFloatBuffer(view));
        glUniformMatrix4fv(modelLoc, false, toFloatBuffer(model));
        glUniform3f(colorLoc, 1f, 0f, 0f);

        glBindVertexArray(vao);
        glDrawElements(GL_TRIANGLES, INDICES.length, GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);

        shader.unbind();
        checkGlError("Renderer.render");
    }

    private void checkGlError(String label) {
        int error = glGetError();
        if (error != GL_NO_ERROR) {
            throw new IllegalStateException(label + " OpenGL error: " + error);
        }
    }

    private FloatBuffer toFloatBuffer(Matrix4f matrix) {
        matrixBuffer.clear();
        matrix.get(matrixBuffer);
        matrixBuffer.flip();
        return matrixBuffer;
    }

    public void cleanup() {
        shader.cleanup();
        glDeleteVertexArrays(vao);
        glDeleteBuffers(vbo);
        if (ebo != 0) glDeleteBuffers(ebo);
    }
}
