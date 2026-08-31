package com.bitwave.ve3.render;

import org.joml.Matrix4f;

import java.util.List;

public record RenderSnapshot(
        List<RenderInstance> instances,
        Matrix4f viewMatrix,
        Matrix4f projectionMatrix
) {
    public record RenderInstance(
            MeshHandle mesh,
            MaterialHandle material,
            Matrix4f modelMatrix
    ) {}
}
