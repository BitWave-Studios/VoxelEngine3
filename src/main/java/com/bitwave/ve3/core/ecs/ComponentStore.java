package com.bitwave.ve3.core.ecs;

import java.util.Arrays;

public class ComponentStore<T extends Component> {
    private static final int INITIAL_CAPACITY = 64;

    private Object[] dense;
    private Entity[] denseToEntity;
    private int[] sparse;
    private int size;

    public ComponentStore() {
        dense = new Object[INITIAL_CAPACITY];
        denseToEntity = new Entity[INITIAL_CAPACITY];
        sparse = new int[INITIAL_CAPACITY];
        Arrays.fill(sparse, -1);
    }

    public void insert(Entity entity, T component) {
        int index = entity.index();
        ensureSparseCapacity(index);

        if (sparse[index] != -1) {
            dense[sparse[index]] = component;
            return;
        }

        ensureDenseCapacity(size);
        dense[size] = component;
        denseToEntity[size] = entity;
        sparse[size] = index;
        size++;
    }

    public void remove(Entity entity) {
        int index = entity.index();
        if (index < 0 || index >= sparse.length || sparse[index] == -1) return;

        int denseIndex = sparse[index];
        int lastIndex = size - 1;

        Object lastComponent = dense[lastIndex];
        Entity lastEntity = denseToEntity[lastIndex];

        dense[denseIndex] = lastComponent;
        denseToEntity[denseIndex] = lastEntity;
        sparse[lastEntity.index()] = denseIndex;

        dense[lastIndex] = null;
        denseToEntity[lastIndex] = null;
        sparse[index] = -1;
        size--;
    }

    public boolean has(Entity entity) {
        int index = entity.index();
        return index >= 0 && index < sparse.length && sparse[index] != -1;
    }

    @SuppressWarnings("unchecked")
    public T get(Entity entity) {
        int index = entity.index();
        if (index < 0 || index >= sparse.length || sparse[index] == -1) return null;
        return (T) dense[sparse[index]];
    }

    public int size() {
        return size;
    }

    public Entity[] denseEntities() {
        return denseToEntity;
    }

    @SuppressWarnings("unchecked")
    public T[] denseComponents() {
        return (T[]) dense;
    }

    private void ensureSparseCapacity(int index) {
        if (index >= sparse.length) {
            int newSize = Math.max(sparse.length * 2, index + 1);
            int oldSize = sparse.length;
            sparse = Arrays.copyOf(sparse, newSize);
            Arrays.fill(sparse, oldSize, newSize, -1);
        }
    }

    private void ensureDenseCapacity(int size) {
        if (size > dense.length) {
            int newSize = dense.length * 2;
            dense = Arrays.copyOf(dense, newSize);
            denseToEntity = Arrays.copyOf(denseToEntity, newSize);
        }
    }
}
