package com.bitwave.ve3.core.ecs.query;

import com.bitwave.ve3.core.ecs.Entity;

@FunctionalInterface
public interface QueryConsumer3<A, B, C> {
    void accept(Entity entity, A a, B b, C c);
}
