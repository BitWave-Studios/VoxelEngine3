package org.bitwave.ve3.gui;

public record UiId(long value) {
    public static final UiId NONE = new UiId(0L);

    public static UiId named(String name) {
        long value = Hashing.hash64(name);
        return value == 0L ? new UiId(1L) : new UiId(value);
    }

    public UiId child(String name) {
        long value = Hashing.combine(this.value, Hashing.hash64(name));
        return value == 0L ? new UiId(1L) : new UiId(value);
    }
}
