package com.bitwave.ve3.core.ecs;

import java.util.ArrayList;
import java.util.List;

public class Scheduler {
    private final List<ECSSystem> systems = new ArrayList<>();

    public void add(ECSSystem system) {
        systems.add(system);
    }

    public void tick(World world, float delta) {
        for (ECSSystem system : systems) {
            system.update(world, delta);
        }
    }
}
