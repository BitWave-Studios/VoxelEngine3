package org.bitwave.ve3.gui;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public final class UiOpenGLRenderer {
    private static final int FLOATS_PER_VERTEX = 8;
    private static final int VERTICES_PER_QUAD = 4;
    private static final int INDICES_PER_QUAD = 6;

    private static final int INITIAL_QUAD_CAPACITY = 4096;

    private final UiShader shader;

    private final int vao;
    private final int vbo;
    private final int ebo;

    private int whiteTexture;

    private FloatBuffer vertexBuffer;
    private IntBuffer indexBuffer;

    private int quadCapacity;
    private int quadCount;

    private int currentTexture;
    private final Deque<UiRect> clipStack = new ArrayDeque<>();

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

        currentTexture = whiteTexture;
        quadCapacity = INITIAL_QUAD_CAPACITY;

        vertexBuffer = BufferUtils.createFloatBuffer(quadCapacity * VERTICES_PER_QUAD * FLOATS_PER_VERTEX);
        indexBuffer = BufferUtils.createIntBuffer(quadCapacity * INDICES_PER_QUAD);
    }

    public void render(UiDrawList drawList, float width, float height) {
        List<UiDrawCommand> commands = drawList.commands();

        if (commands.isEmpty()) {
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
                case UiDrawCommand.TexturedRect texturedRect -> drawTexturedRect(texturedRect);
                case UiDrawCommand.Text text -> drawText(text);
                case UiDrawCommand.Line line -> drawLine(line);
                case UiDrawCommand.PushClip clip -> pushClip(clip.rect(), height);
                case UiDrawCommand.PopClip ignored -> popClip(height);
                default -> throw new IllegalStateException("Unexpected value: " + command);
            }
        }

        flush();

        clipStack.clear();

        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        glDisable(GL_BLEND);
        glDisable(GL_SCISSOR_TEST);

        glBindVertexArray(0);
        shader.unbind();
    }

    private void drawFilledRect(UiDrawCommand.FilledRect command) {
        UiRect rect = command.rect();
        UiColor color = command.color();

        setTexture(whiteTexture);

        addQuad(
                rect.x(),
                rect.y(),
                0.0f, 0.0f,

                rect.x() + rect.width(),
                rect.y(),
                1.0f, 0.0f,

                rect.x() + rect.width(),
                rect.y() + rect.height(),
                1.0f, 1.0f,

                rect.x(),
                rect.y() + rect.height(),
                0.0f, 1.0f,

                color
        );

        // TODO: Implement rounded corners
        // For now, we will ignore the cornerRadius parameter and draw a simple filled rectangle.
    }

    private void drawTexturedRect(UiDrawCommand.TexturedRect command) {
        UiRect rect = command.rect();
        UiTexture texture = command.texture();
        UiColor tint = command.tint();

        setTexture(texture.id());

        addQuad(
                rect.x(),
                rect.y(),
                0.0f, 0.0f,

                rect.x() + rect.width(),
                rect.y(),
                1.0f, 0.0f,

                rect.x() + rect.width(),
                rect.y() + rect.height(),
                1.0f, 1.0f,

                rect.x(),
                rect.y() + rect.height(),
                0.0f, 1.0f,

                tint
        );
    }

    private void drawText(UiDrawCommand.Text command) {
        UiFont font = command.font();
        String value = command.value();
        UiColor color = command.color();
        float fontSize = command.fontSize();

        if (font == null || value == null || value.isEmpty()) {
            return;
        }

        setTexture(font.atlas().id());

        float penX = command.x();
        float penY = command.y();

        for (int i = 0; i < value.length(); i++) {
            char character = value.charAt(i);

            if (character == '\n') {
                penX = command.x();
                penY += font.lineHeight(fontSize);
                continue;
            }

            UiGlyph glyph = font.glyph(character, fontSize);

            if (glyph == null) {
                font.advance(character, fontSize);
                continue;
            }

            float glyphLeft = penX + glyph.bearingX();
            float glyphTop = penY + glyph.bearingY();
            float glyphRight = glyphLeft + glyph.width();
            float glyphBottom = glyphTop + glyph.height();

            addQuad(
                    glyphLeft,
                    glyphTop,
                    glyph.u0(),
                    glyph.v0(),

                    glyphRight,
                    glyphTop,
                    glyph.u1(),
                    glyph.v0(),

                    glyphRight,
                    glyphBottom,
                    glyph.u1(),
                    glyph.v1(),

                    glyphLeft,
                    glyphBottom,
                    glyph.u0(),
                    glyph.v1(),

                    color
            );

            penX += glyph.advance();
        }
    }

    private void drawLine(UiDrawCommand.Line command) {
        float x1 = command.x1();
        float y1 = command.y1();
        float x2 = command.x2();
        float y2 = command.y2();

        float thickness = Math.max(command.thickness(), 1.0f);
        UiColor color = command.color();

        float dx = x2 - x1;
        float dy = y2 - y1;

        float length = (float) Math.sqrt(dx * dx + dy * dy);

        if (length <= 0.0001f) {
            return;
        }

        float normalX = -dy / length;
        float normalY = dx / length;

        float halfThickness = thickness * 0.5f;

        float ax = x1 + normalX * halfThickness;
        float ay = y1 + normalY * halfThickness;

        float bx = x2 + normalX * halfThickness;
        float by = y2 + normalY * halfThickness;

        float cx = x2 - normalX * halfThickness;
        float cy = y2 - normalY * halfThickness;

        float dx2 = x1 - normalX * halfThickness;
        float dy2 = y1 - normalY * halfThickness;

        setTexture(whiteTexture);

        addQuad(
                ax, ay, 0.0f, 0.0f,
                bx, by, 1.0f, 0.0f,
                cx, cy, 1.0f, 1.0f,
                dx2, dy2, 0.0f, 1.0f,
                color
        );
    }

    private void addQuad(
            float x0, float y0, float u0, float v0,
            float x1, float y1, float u1, float v1,
            float x2, float y2, float u2, float v2,
            float x3, float y3, float u3, float v3,
            UiColor color
    ) {
        if (quadCount >= quadCapacity) {
            flush();
        }

        float red = color.r();
        float green = color.g();
        float blue = color.b();
        float alpha = color.a();

        vertexBuffer.put(x0).put(y0).put(u0).put(v0)
                .put(red).put(green).put(blue).put(alpha);

        vertexBuffer.put(x1).put(y1).put(u1).put(v1)
                .put(red).put(green).put(blue).put(alpha);

        vertexBuffer.put(x2).put(y2).put(u2).put(v2)
                .put(red).put(green).put(blue).put(alpha);

        vertexBuffer.put(x3).put(y3).put(u3).put(v3)
                .put(red).put(green).put(blue).put(alpha);

        int baseVertex = quadCount * VERTICES_PER_QUAD;

        indexBuffer.put(baseVertex);
        indexBuffer.put(baseVertex + 1);
        indexBuffer.put(baseVertex + 2);
        indexBuffer.put(baseVertex + 2);
        indexBuffer.put(baseVertex + 3);
        indexBuffer.put(baseVertex);

        quadCount++;
    }

    private void setTexture(int texture) {
        if (texture != currentTexture) {
            flush();
            currentTexture = texture;
        }
    }

    private void flush() {
        if (quadCount == 0) {
            return;
        }

        vertexBuffer.flip();
        indexBuffer.flip();

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBindTexture(GL_TEXTURE_2D, currentTexture);

        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_DYNAMIC_DRAW);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_DYNAMIC_DRAW);

        glDrawElements(
                GL_TRIANGLES,
                quadCount * INDICES_PER_QUAD,
                GL_UNSIGNED_INT,
                0
        );

        vertexBuffer.clear();
        indexBuffer.clear();
        quadCount = 0;
    }

    private void pushClip(UiRect rect, float windowHeight) {
        UiRect parent = clipStack.peek();
        UiRect clipped = parent == null
                ? rect
                : intersect(parent, rect);

        clipStack.push(clipped);
        applyScissor(clipped, windowHeight);
    }

    private void popClip(float windowHeight) {
        if (clipStack.isEmpty()) {
            throw new IllegalStateException("Unbalanced UiDrawCommand.PopClip");
        }

        clipStack.pop();

        UiRect parent = clipStack.peek();

        if (parent == null) {
            glDisable(GL_SCISSOR_TEST);
        } else {
            applyScissor(parent, windowHeight);
        }
    }

    private void applyScissor(UiRect rect, float windowHeight) {
        int x = Math.max(0, (int) rect.x());
        int y = Math.max(
                0,
                (int) (windowHeight - rect.y() - rect.height())
        );

        int width = Math.max(0, (int) rect.width());
        int height = Math.max(0, (int) rect.height());

        glEnable(GL_SCISSOR_TEST);
        glScissor(x, y, width, height);
    }

    private static UiRect intersect(UiRect a, UiRect b) {
        float left = Math.max(a.x(), b.x());
        float top = Math.max(a.y(), b.y());

        float right = Math.min(
                a.x() + a.width(),
                b.x() + b.width()
        );

        float bottom = Math.min(
                a.y() + a.height(),
                b.y() + b.height()
        );

        float width = Math.max(0.0f, right - left);
        float height = Math.max(0.0f, bottom - top);

        return new UiRect(left, top, width, height);
    }

    private int createWhiteTexture() {
        int texture = glGenTextures();

        glBindTexture(GL_TEXTURE_2D, texture);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

        var pixels = BufferUtils.createByteBuffer(4);

        pixels.put((byte) 255)
                .put((byte) 255)
                .put((byte) 255)
                .put((byte) 255)
                .flip();

        glTexImage2D(
                GL_TEXTURE_2D,
                0,
                GL_RGBA,
                1,
                1,
                0,
                GL_RGBA,
                GL_UNSIGNED_BYTE,
                pixels
        );

        return texture;
    }

    public void cleanup() {
        if (whiteTexture != 0) {
            glDeleteTextures(whiteTexture);
            whiteTexture = 0;
        }

        glDeleteVertexArrays(vao);
        glDeleteBuffers(vbo);
        glDeleteBuffers(ebo);
    }
}
