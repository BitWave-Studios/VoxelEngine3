package com.bitwave.ve3.core.ecs;

import com.bitwave.ve3.core.ecs.query.Query1;
import com.bitwave.ve3.core.ecs.query.Query2;
import com.bitwave.ve3.core.ecs.query.Query3;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class World {
    private final EntityAllocator allocator = new EntityAllocator();
    private final Map<Class<?>, ComponentStore<?>> stores = new HashMap<>();

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
        @SuppressWarnings("unchecked")
        Class<T> type = (Class<T>) component.getClass();
        storeFor(type).insert(entity, component);
    }

    public <T extends Component> void remove(Entity entity, Class<T> type) {
        storeFor(type).remove(entity);
    }

    public <T extends Component> Optional<T> get(Entity entity, Class<T> type) {
        return Optional.ofNullable(storeFor(type).get(entity));
    }

    public <T extends Component> boolean has(Entity entity, Class<T> type) {
        ComponentStore<?> store = stores.get(type);
        return store != null && store.has(entity);
    }

    @SuppressWarnings("unchecked")
    public <T extends Component> ComponentStore<T> storeFor(Class<T> type) {
        return (ComponentStore<T>) stores.computeIfAbsent(type, t -> new ComponentStore<>());
    }

    @SuppressWarnings("unchecked")
    public <T extends Component> ComponentStore<T> storeForQuery(Class<T> type) {
        return (ComponentStore<T>) stores.computeIfAbsent(type, t -> new ComponentStore<>());
    }

    public <A extends Component> Query1<A> query(Class<A> typeA) {
        return new Query1<>(this, typeA);
    }

    public <A extends Component, B extends Component> Query2<A, B> query(Class<A> typeA, Class<B> typeB) {
        return new Query2<>(this, typeA, typeB);
    }

    public <A extends Component, B extends Component, C extends Component> Query3<A, B, C> query(Class<A> typeA, Class<B> typeB, Class<C> typeC) {
        return new Query3<>(this, typeA, typeB, typeC);
    }

    private void requireAlive(Entity entity) {
        if (!allocator.isAlive(entity)) {
            throw new IllegalStateException("Entity is not alive: " + entity);
        }
    }
}
