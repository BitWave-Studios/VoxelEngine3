package com.bitwave.ve3.core.ecs.component;

import com.bitwave.ve3.core.ecs.Component;
import com.bitwave.ve3.render.MaterialHandle;
import com.bitwave.ve3.render.MeshHandle;

public record MeshRenderer(MeshHandle mesh, MaterialHandle material) implements Component {
}
