package com.bitwave.ve3.core.ecs;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity allocation and management.
 */
public class EntityAllocator {
    private final List<Integer> generations = new ArrayList<>();
    private final List<Integer> freeList = new ArrayList<>();

    public Entity spawn() {
        if (!freeList.isEmpty()) {
            int index = freeList.remove(freeList.size() - 1);
            return new Entity(index, generations.get(index));
        }
        int index = generations.size();
        generations.add(0);
        return new Entity(index, 0);
    }

    public void despawn(Entity entity) {
        if (!isAlive(entity)) return;
        generations.set(entity.index(), entity.generation() + 1);
        freeList.add(entity.index());
    }

    public boolean isAlive(Entity entity) {
        if (entity.index() < 0 || entity.index() >= generations.size()) return false;
        return generations.get(entity.index()) == entity.generation();
    }
}
