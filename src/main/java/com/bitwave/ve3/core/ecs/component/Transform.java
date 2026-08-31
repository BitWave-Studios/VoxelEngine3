package com.bitwave.ve3.core.ecs.component;

import com.bitwave.ve3.core.ecs.Component;
import org.joml.Quaternionf;
import org.joml.Vector3f;

/**
 * Entity/Object transformation in the 3D space
 * @param position world pos in the 3D environment
 * @param rotation world rotation in the 3D environment
 * @param scale x, y and z scale/size in the 3D environment
 */
public record Transform(Vector3f position, Quaternionf rotation, Vector3f scale) implements Component {
    public static Transform identity() {
        return new Transform(new Vector3f(0, 0, 0), new Quaternionf(), new Vector3f(1, 1, 1));
    }
}
