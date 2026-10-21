package org.bitwave.facet;

import java.util.Objects;

/**
 * Platform-neutral handle for a compiled shader program.
 *
 * <p>Renderer backend owns the native shader object; this type only exposes
 * a safe identifier to engine and game code.</p>
 */
public final class ShaderProgram {
    private final int handle;

    public ShaderProgram(int handle) {
        if (handle <= 0) {
            throw new IllegalArgumentException("Shader program handle must be a positive integer");
        }

        this.handle = handle;
    }

    public int handle() {
        return handle;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ShaderProgram other && handle == other.handle;
    }

    @Override
    public int hashCode() {
        return Objects.hash(handle);
    }
}
