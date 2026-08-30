package com.bitwave.ve3.core.ecs.query;

import com.bitwave.ve3.core.ecs.Component;
import com.bitwave.ve3.core.ecs.ComponentStore;
import com.bitwave.ve3.core.ecs.Entity;
import com.bitwave.ve3.core.ecs.World;

public class Query1<A extends Component> {
    private final World world;
    private final Class<A> typeA;

    public Query1(World world, Class<A> typeA) {
        this.world = world;
        this.typeA = typeA;
    }

    public void forEach(QueryConsumer1<A> consumer) {
        ComponentStore<A> storeA = world.storeForQuery(typeA);
        Entity[] entities = storeA.denseEntities();
        int size = storeA.size();

        for (int i = 0; i < size; i++) {
            Entity entity = entities[i];
            A a = storeA.get(entity);
            if (a != null) {
                consumer.accept(entity, a);
            }
        }
    }
}
