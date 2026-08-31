package com.bitwave.ve3.render;

import java.util.HashMap;
import java.util.Map;

public class ResourceRegistry<T> {
    private final Map<Integer, T> resources = new HashMap<>();
    private int nextId = 0;

    public int register(T resource) {
        int id = nextId++;
        resources.put(id, resource);
        return id;
    }

    public T get(int id) {
        T resource = resources.get(id);
        if (resource == null) {
            throw new IllegalArgumentException("No resource registered with id " + id);
        }
        return resource;
    }
}
