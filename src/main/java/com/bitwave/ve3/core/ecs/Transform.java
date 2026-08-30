package com.bitwave.ve3.core.ecs;

import org.joml.Quaternionf;
import org.joml.Vector3f;

public record Transform(Vector3f position, Quaternionf rotation, Vector3f scale) implements Component {
}
