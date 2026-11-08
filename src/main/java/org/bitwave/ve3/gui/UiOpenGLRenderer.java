package org.bitwave.ve3.gui;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.*;

public final class UiOpenGLRenderer {
    private static final int FLOATS_PER_VERTEX = 8;
    private static final int VERTICES_PER_QUAD = 4;
    private static final int INDICES_PER_QUAD = 6;

    private final UiShader shader;

    private final int vao;
    private final int vbo;
    private final int ebo;

    private int whiteTexture;

    private FloatBuffer vertexBuffer;
    private IntBuffer indexBuffer;

    public UiOpenGLRenderer(UiShader shader) {
        this.shader = shader;

        vao = glGenVertexArrays();
        vbo = glGenBuffers();
        ebo = glGenBuffers();

        glBindVertexArray(vao);

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);

        int stride = FLOATS_PER_VERTEX * Float.BYTES;

        glVertexAttribPointer(0, 2, GL_FLOAT, false, stride, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, 2, GL_FLOAT, false, stride, 2 * Float.BYTES);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, 4, GL_FLOAT, false, stride, 4 * Float.BYTES);
        glEnableVertexAttribArray(2);

        glBindVertexArray(0);

        whiteTexture = createWhiteTexture();

        vertexBuffer = memAllocFloat(1024 * FLOATS_PER_VERTEX);
        indexBuffer = memAllocInt(1024);
    }

    public void render(UiDrawList drawList, float width, float height) {
        if (drawList.commands().isEmpty()) {
            return;
        }

        shader.bind();
        shader.setProjection(width, height);

        glBlendFuncSeparate(
                GL_SRC_ALPHA,
                GL_ONE_MINUS_SRC_ALPHA,
                GL_ONE,
                GL_ONE_MINUS_SRC_ALPHA
        );

        glDisable(GL_DEPTH_TEST);
        glDisable(GL_CULL_FACE);
        glEnable(GL_BLEND);

        glBindVertexArray(vao);

        for (UiDrawCommand command : drawList.commands()) {
            switch (command) {
                case UiDrawCommand.FilledRect filledRect -> drawFilledRect(filledRect);
                case UiDrawCommand.Text text -> {

                }
                case UiDrawCommand.Line line -> {

                }
                case UiDrawCommand.PushClip clip -> applyScissor(clip.rect(), height);
                case UiDrawCommand.PopClip ignored -> glDisable(GL_SCISSOR_TEST);
            }
        }

        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        glDisable(GL_BLEND);

        glBindVertexArray(0);
        shader.unbind();
    }

    private void drawFilledRect(UiDrawCommand.FilledRect command) {
        UiRect rect = command.rect();
        UiColor color = command.color();

        float left = rect.x();
        float right = rect.x() + rect.width();
        float top = rect.y();
        float bottom = rect.y() + rect.height();

        float[] vertices = {
                left, top, 0, 0, color.r(), color.g(), color.b(), color.a(),
                right, top, 1, 0, color.r(), color.g(), color.b(), color.a(),
                right, bottom, 1, 1, color.r(), color.g(), color.b(), color.a(),
                left, bottom, 0, 1, color.r(), color.g(), color.b(), color.a()
        };

        int[] indices = {0, 1, 2, 2, 3, 0};

        vertexBuffer.clear();
        vertexBuffer.put(vertices).flip();

        indexBuffer.clear();
        indexBuffer.put(indices).flip();

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_DYNAMIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_DYNAMIC_DRAW);

        glBindTexture(GL_TEXTURE_2D, whiteTexture);
        glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
    }

    private void applyScissor(UiRect rect, float windowHeight) {
        glEnable(GL_SCISSOR_TEST);

        glScissor(
                (int) rect.x(),
                (int) (windowHeight - rect.y() - rect.height()),
                (int) rect.width(),
                (int) rect.height()
        );
    }

    private int createWhiteTexture() {
        int texture = glGenTextures();

        glBindTexture(GL_TEXTURE_2D, texture);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

        ByteBuffer pixels = memAlloc(4);
        pixels.put((byte) 255)
                .put((byte) 255)
                .put((byte) 255)
                .put((byte) 255)
                .flip();

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, 1, 1, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);

        memFree(pixels);

        return texture;
    }

    public void cleanup() {
        memFree(vertexBuffer);
        memFree(indexBuffer);

        glDeleteTextures(whiteTexture);
        glDeleteVertexArrays(vao);
        glDeleteBuffers(vbo);
        glDeleteBuffers(ebo);
    }
}
