package org.bitwave.ve3.core.world;

import java.util.Objects;

/**
 * Represents the position of a chunk in the world.
 * @param x The x-coordinate of the chunk.
 * @param z The z-coordinate of the chunk.
 * @author k4i
 */
public record ChunkPos(int x, int z) {
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ChunkPos other)) return false;
        return x == other.x && z == other.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, z);
    }
}
