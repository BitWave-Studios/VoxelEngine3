package com.bitwave.ve3.render;

import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

public class Shader {
    private final int program;
    private final Map<String, Integer> uniformLocations = new HashMap<>();

    public Shader(String vertSrc, String fragSrc) {
        int vertex = compile(GL_VERTEX_SHADER, vertSrc);
        int fragment = compile(GL_FRAGMENT_SHADER, fragSrc);

        program = glCreateProgram();
        glAttachShader(program, vertex);
        glAttachShader(program, fragment);
        glLinkProgram(program);

        if (glGetProgrami(program, GL_LINK_STATUS) == GL_FALSE) {
            throw new IllegalStateException("Shader link failed: " + glGetShaderInfoLog(program));
        }

        glDeleteShader(vertex);
        glDeleteShader(fragment);
    }

    private int compile(int type, String source) {
        int shader = glCreateShader(type);
        glShaderSource(shader, source);
        glCompileShader(shader);

        if (glGetShaderi(shader, GL_COMPILE_STATUS) == GL_FALSE) {
            throw new IllegalStateException("Shader compile failed: " + glGetShaderInfoLog(shader));
        }
        return shader;
    }

    public void bind() {
        glUseProgram(program);
    }

    public void setUniform(String name, Matrix4f matrix) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer buffer = matrix.get(stack.mallocFloat(16));
            glUniformMatrix4fv(location(name), false, buffer);
        }
    }

    public void setUniform(String name, float r, float g, float b) {
        glUniform3f(location(name), r, g, b);
    }

    private int location(String name) {
        return uniformLocations.computeIfAbsent(name, n -> glGetUniformLocation(program, n));
    }

    public void destroy() {
        glDeleteProgram(program);
    }
}
