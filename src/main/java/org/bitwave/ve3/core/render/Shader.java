package org.bitwave.ve3.core.render;

import static org.lwjgl.opengl.GL20.*;

public class Shader {
    private final int programId;

    public Shader(String vertexSrc, String fragmentSrc) {
        int v = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(v, vertexSrc);
        glCompileShader(v);
        checkCompile(v);

        int f = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(f, fragmentSrc);
        glCompileShader(f);
        checkCompile(f);

        programId = glCreateProgram();
        glAttachShader(programId, v);
        glAttachShader(programId, f);
        glLinkProgram(programId);
        if (glGetProgrami(programId, GL_LINK_STATUS) == GL_FALSE) {
            throw new RuntimeException("Shader link failed: " + glGetProgramInfoLog(programId));
        }

        // Cleanup after linking
        glDeleteShader(v);
        glDeleteShader(f);
    }

    private void checkCompile(int shader) {
        if (glGetShaderi(shader, GL_COMPILE_STATUS) == GL_FALSE) {
            String info = glGetShaderInfoLog(shader);
            throw new RuntimeException("Shader compile failed: " + info);
        }
    }

    public void bind() {
        glUseProgram(programId);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public int getId() {
        return programId;
    }

    public void cleanup() {
        glDeleteProgram(programId);
    }
}
