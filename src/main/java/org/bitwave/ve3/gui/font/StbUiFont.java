package org.bitwave.ve3.gui.font;

import org.bitwave.ve3.core.util.ResourcePaths;
import org.bitwave.ve3.gui.UiFont;
import org.bitwave.ve3.gui.UiGlyph;
import org.bitwave.ve3.gui.UiTexture;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBTTFontinfo;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.stb.STBTruetype.*;
import static org.lwjgl.system.MemoryUtil.*;

public final class StbUiFont implements UiFont, AutoCloseable {
    private static final int FIRST_CHAR = 32;
    private static final int LAST_CHAR = 126;
    private static final int BASE_SIZE = 48;

    private static final int CELL_WIDTH = 64;
    private static final int CELL_HEIGHT = 64;
    private static final int COLUMNS = 16;
    private static final int ROWS = 6;
    private static final int ATLAS_WIDTH = COLUMNS * CELL_WIDTH;
    private static final int ATLAS_HEIGHT = ROWS * CELL_HEIGHT;

    private final UiTexture atlas;
    private final Map<Character, UiGlyph> glyphs = new HashMap<>();
    private final float baseSize;
    private final float lineHeightAtBaseSize;

    public StbUiFont(String resourcePath) {
        this(resourcePath, BASE_SIZE);
    }

    public StbUiFont(String resourcePath, float baseSize) {
        if (baseSize <= 0) {
            throw new IllegalArgumentException("baseSize must be positve");
        }

        this.baseSize = baseSize;

        ByteBuffer fontData = null;
        STBTTFontinfo fontInfo = null;
        int textureId = 0;

        try {
            Path path = Path.of(ResourcePaths.toFilePath(resourcePath));
            byte[] bytes = Files.readAllBytes(path);

            fontData = memAlloc(bytes.length);
            fontData.put(bytes).flip();

            fontInfo = STBTTFontinfo.malloc();
            if (!stbtt_InitFont(fontInfo, fontData)) {
                throw new IllegalArgumentException("Could not parse font: " + resourcePath);
            }

            float scale = stbtt_ScaleForPixelHeight(fontInfo, baseSize);

            IntBuffer ascent = BufferUtils.createIntBuffer(1);
            IntBuffer descent = BufferUtils.createIntBuffer(1);
            IntBuffer lineGap = BufferUtils.createIntBuffer(1);
            stbtt_GetFontVMetrics(fontInfo, ascent, descent, lineGap);

            lineHeightAtBaseSize = (ascent.get(0) - descent.get(0) + lineGap.get(0)) * scale;

            ByteBuffer rgba = BufferUtils.createByteBuffer(ATLAS_WIDTH * ATLAS_HEIGHT * 4);
            for (int i = 0; i < ATLAS_WIDTH * ATLAS_HEIGHT; i++) {
                rgba.put((byte) 255).put((byte) 255).put((byte) 255).put((byte) 0);
            }

            for (int code = FIRST_CHAR; code <= LAST_CHAR; code++) {
                int cell = code - FIRST_CHAR;
                int cellX = (cell % COLUMNS) * CELL_WIDTH;
                int cellY = (cell / COLUMNS) * CELL_HEIGHT;

                IntBuffer w = BufferUtils.createIntBuffer(1);
                IntBuffer h = BufferUtils.createIntBuffer(1);
                IntBuffer xoff = BufferUtils.createIntBuffer(1);
                IntBuffer yoff = BufferUtils.createIntBuffer(1);

                ByteBuffer bitmap = stbtt_GetCodepointBitmap(fontInfo, scale, scale, code, w, h, xoff, yoff);

                int glyphWidth = w.get(0);
                int glyphHeight = h.get(0);

                if (glyphWidth > CELL_WIDTH || glyphHeight > CELL_HEIGHT) {
                    if (bitmap != null) throw new IllegalArgumentException(
                            "Glyph exceeds atlas cell; increase cell size or reduce base size"
                    );
                }

                if (bitmap != null) {
                    for (int y = 0; y < glyphHeight; y++) {
                        for (int x = 0; x < glyphWidth; x++) {
                            int alpha = bitmap.get(y * glyphWidth + x) & 0xFF;
                            int atlasIndex = ((cellY + y) * ATLAS_WIDTH + cellX + x) * 4;
                            rgba.put(atlasIndex + 3, (byte) alpha);
                        }
                    }

                    stbtt_FreeBitmap(bitmap);
                }

                IntBuffer advanceWidth = BufferUtils.createIntBuffer(1);
                IntBuffer leftSideBearing = BufferUtils.createIntBuffer(1);
                stbtt_GetCodepointHMetrics(fontInfo, code, advanceWidth, leftSideBearing);

                float advance = advanceWidth.get(0) * scale;

                glyphs.put((char) code, new UiGlyph(
                        glyphWidth,
                        glyphHeight,
                        xoff.get(0),
                        yoff.get(0),
                        advance,
                        (float) cellX / ATLAS_WIDTH,
                        (float) cellY / ATLAS_HEIGHT,
                        (float) (cellX + glyphWidth) / ATLAS_WIDTH,
                        (float) (cellY + glyphHeight) / ATLAS_HEIGHT
                ));
            }

            rgba.flip();

            textureId = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, textureId);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8,
                    ATLAS_WIDTH, ATLAS_HEIGHT, 0,
                    GL_RGBA, GL_UNSIGNED_BYTE, rgba
            );

            glBindTexture(GL_TEXTURE_2D, 0);
            atlas = new UiTexture(textureId);
        } catch (Exception e) {
            if (textureId != 0) {
                glDeleteTextures(textureId);
            }
            throw new IllegalStateException("Failed to load font: " + resourcePath, e);
        } finally {
            if (fontInfo != null) {
                fontInfo.free();
            }
            if (fontData != null) {
                memFree(fontData);
            }
        }
    }

    @Override
    public UiTexture atlas() {
        return atlas;
    }

    @Override
    public float lineHeight(float fontSize) {
        return lineHeightAtBaseSize * (fontSize / baseSize);
    }

    @Override
    public float advance(char character, float fontSize) {
        UiGlyph glyph = glyphs.get(character);
        if (glyph == null) {
            glyph = glyphs.get('?');
        }
        return glyph == null ? fontSize * 0.5f : glyph.advance() * (fontSize / baseSize);
    }

    @Override
    public UiGlyph glyph(char character, float fontSize) {
        UiGlyph glyph = glyphs.get(character);
        if (glyph == null) {
            glyph = glyphs.get('?');
        }
        if (glyph == null) {
            return null;
        }

        float scale = fontSize / baseSize;
        return new UiGlyph(
                glyph.width() * scale,
                glyph.height() * scale,
                glyph.bearingX() * scale,
                glyph.bearingY() * scale,
                glyph.advance() * scale,
                glyph.u0(), glyph.v0(), glyph.u1(), glyph.v1()
        );
    }

    @Override
    public void close() throws Exception {
        glDeleteTextures(atlas.id());
    }
}
