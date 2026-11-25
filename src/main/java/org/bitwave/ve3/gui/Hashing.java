package org.bitwave.ve3.gui;

public final class Hashing {
    private static final long FNV_OFFSET_BASIS = 0xcbf29ce484222325L;
    private static final long FNV_PRIME = 0x100000001b3L;

    private Hashing() {}

    public static long hash64(String text) {
        if (text == null) {
            throw new IllegalArgumentException("Input text cannot be null");
        }

        long hash = FNV_OFFSET_BASIS;
        for (int i = 0; i < text.length(); i++) {
            hash ^= text.charAt(i);
            hash *= FNV_PRIME;
        }
        return hash;
    }

    public static long combine(long parent, long child) {
        long hash = parent ^ (child + 0x9e3779b97f4a7c15L + (parent << 6) + (parent >>> 2));

        hash ^= hash >>> 30;
        hash *= 0xbf58476d1ce4e5b9L;
        hash ^= hash >>> 27;
        hash *= 0x94d049bb133111ebL;
        hash ^= hash >>> 31;
        return hash;
    }
}
