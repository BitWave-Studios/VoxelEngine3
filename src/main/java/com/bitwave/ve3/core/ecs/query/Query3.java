package com.bitwave.ve3.core.ecs.query;

import com.bitwave.ve3.core.ecs.Component;
import com.bitwave.ve3.core.ecs.ComponentStore;
import com.bitwave.ve3.core.ecs.Entity;
import com.bitwave.ve3.core.ecs.World;

public class Query3<A extends Component, B extends Component, C extends Component> {
    private final World world;
    private final Class<A> typeA;
    private final Class<B> typeB;
    private final Class<C> typeC;

    public Query3(World world, Class<A> typeA, Class<B> typeB, Class<C> typeC) {
        this.world = world;
        this.typeA = typeA;
        this.typeB = typeB;
        this.typeC = typeC;
    }

    public void forEach(QueryConsumer3<A, B, C> consumer) {
        ComponentStore<A> storeA = world.storeForQuery(typeA);
        ComponentStore<B> storeB = world.storeForQuery(typeB);
        ComponentStore<C> storeC = world.storeForQuery(typeC);

        ComponentStore<?> smallest = storeA;
        if (storeB.size() < smallest.size()) smallest = storeB;
        if (storeC.size() < smallest.size()) smallest = storeC;

        Entity[] entities = smallest.denseEntities();
        int size = smallest.size();

        for (int i = 0; i < size; i++) {
            Entity entity = entities[i];
            A a = storeA.get(entity);
            B b = storeB.get(entity);
            C c = storeC.get(entity);
            if (a != null && b != null && c != null) {
                consumer.accept(entity, a, b, c);
            }
        }
    }
}
