package com.bitwave.ve3.core.ecs;

public record Entity(int index, int generation) {
    public static final Entity NULL = new Entity(-1, 0);
}
