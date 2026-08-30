package com.bitwave.ve3.core.ecs.query;

import com.bitwave.ve3.core.ecs.Entity;

@FunctionalInterface
public interface QueryConsumer1<A> {
    void accept(Entity entity, A a);
}
