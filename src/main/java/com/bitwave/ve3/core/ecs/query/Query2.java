package com.bitwave.ve3.core.ecs.query;

import com.bitwave.ve3.core.ecs.Component;
import com.bitwave.ve3.core.ecs.ComponentStore;
import com.bitwave.ve3.core.ecs.Entity;
import com.bitwave.ve3.core.ecs.World;

public class Query2<A extends Component, B extends Component> {
    private final World world;
    private final Class<A> typeA;
    private final Class<B> typeB;

    public Query2(World world, Class<A> typeA, Class<B> typeB) {
        this.world = world;
        this.typeA = typeA;
        this.typeB = typeB;
    }

    public void forEach(QueryConsumer2<A, B> consumer) {
        ComponentStore<A> storeA = world.storeForQuery(typeA);
        ComponentStore<B> storeB = world.storeForQuery(typeB);

        ComponentStore<?> smaller = storeA.size() <= storeB.size() ? storeA : storeB;
        boolean aIsSmaller = smaller == storeA;

        Entity[] entities = smaller.denseEntities();
        int size = smaller.size();

        for (int i = 0; i < size; i++) {
            Entity entity = entities[i];
            A a = aIsSmaller ? storeA.get(entity) : storeA.get(entity);
            B b = storeB.get(entity);
            if (a != null && b != null) {
                consumer.accept(entity, a, b);
            }
        }
    }
}
