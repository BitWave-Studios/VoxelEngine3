package com.bitwave.ve3.core.ecs.component;

import com.bitwave.ve3.core.ecs.Component;

public record Camera(float fovDegrees, float nearPlane, float farPlane) implements Component {
    public static Camera perspectiveDefault() {
        return new Camera(70f, 0.1f, 1000f);
    }
}
