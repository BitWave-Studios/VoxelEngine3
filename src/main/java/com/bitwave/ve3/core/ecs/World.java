package com.bitwave.ve3.core.ecs;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class World {
    private final EntityAllocator allocator = new EntityAllocator();
    private final Map<Class<? extends Component>, ComponentStore<?>> stores = new HashMap<>();

    public Entity spawn() {
        return allocator.spawn();
    }

    public void despawn(Entity entity) {
        for (ComponentStore<?> store : stores.values()) {
            store.remove(entity);
        }
        allocator.despawn(entity);
    }

    public boolean isAlive(Entity entity) {
        return allocator.isAlive(entity);
    }

    public <T extends Component> void add(Entity entity, T component) {
        requireAlive(entity);
        storeFor(component.getClass()).insert(entity, component);
    }

    @SuppressWarnings("unchecked")
    public <T extends Component> void remove(Entity entity, Class<T> type) {
        ComponentStore<T> store = (ComponentStore<T>) stores.get(type);
        if (store != null) store.remove(entity);
    }

    @SuppressWarnings("unchecked")
    public <T extends Component> Optional<T> get(Entity entity, Class<T> type) {
        ComponentStore<T> store = (ComponentStore<T>) stores.get(type);
        if (store == null) return Optional.empty();
        return Optional.ofNullable(store.get(entity));
    }

    public <T extends Component> boolean has(Entity entity, Class<T> type) {
        ComponentStore<?> store = stores.get(type);
        return store != null && store.has(entity);
    }

    @SuppressWarnings("unchecked")
    <T extends Component> ComponentStore<T> storeFor(Class<?> type) {
        return (ComponentStore<T>) stores.computeIfAbsent(type, t -> new ComponentStore<>());
    }

    @SuppressWarnings("unchecked")
    <T extends Component> ComponentStore<T> storeForQuery(Class<T> type) {
        return (ComponentStore<T>) stores.computeIfAbsent(type, t -> new ComponentStore<>());
    }

    private void requireAlive(Entity entity) {
        if (!allocator.isAlive(entity)) {
            throw new IllegalStateException("Entity is not alive: " + entity);
        }
    }
}
