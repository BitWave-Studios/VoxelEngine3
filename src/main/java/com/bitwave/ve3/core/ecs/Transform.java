package com.bitwave.ve3.core.ecs;

public record Transform(Vector3f position, Quaternionf rotation, Vector3f scale) implements Component {
}
