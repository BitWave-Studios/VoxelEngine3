package org.bitwave.ve3.core.world;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple world representation that manages chunks and blocks.
 * @author k4i
 */
public class World {
    private final Map<ChunkPos, Chunk> chunks = new HashMap<>();

    public Chunk getOrCreateChunk(int chunkX, int chunkZ) {
        ChunkPos pos = new ChunkPos(chunkX, chunkZ);
        return chunks.computeIfAbsent(pos, ignored -> new Chunk());
    }

    public void setBlock(int worldX, int worldY, int worldZ, byte id) {
        int chunkX = worldX / Chunk.SIZE;
        int chunkZ = worldZ / Chunk.SIZE;

        int localX = worldX - chunkX * Chunk.SIZE;
        int localZ = worldZ - chunkZ * Chunk.SIZE;

        Chunk chunk = getOrCreateChunk(chunkX, chunkZ);
        chunk.setBlock(localX, worldY, localZ, id);
    }

    public byte getBlock(int worldX, int worldY, int worldZ) {
        int chunkX = worldX / Chunk.SIZE;
        int chunkZ = worldZ / Chunk.SIZE;

        int localX = worldX - chunkX * Chunk.SIZE;
        int localZ = worldZ - chunkZ * Chunk.SIZE;

        Chunk chunk = chunks.get(new ChunkPos(chunkX, chunkZ));
        if (chunk == null) return 0;
        return chunk.getBlock(localX, worldY, localZ);
    }

    public boolean inBounds(int x, int y, int z) {
        return y >= 0 && y < Chunk.HEIGHT;
    }

    public void generateFlatWorld(int sizeX, int sizeZ) {
        for (int x = 0; x < sizeX; x++) {
            for (int z = 0; z < sizeZ; z++) {
                for (int y = 0; y < 3; y++) {
                    setBlock(x, y, z, (byte) 1); // Dirt
                }
                setBlock(x, 3, z, (byte) 2); // Grass
            }
        }
    }

    public boolean isSolid(int x, int y, int z) {
        if (!inBounds(x,y,z)) return false;
        return getBlock(x, y, z) != 0;
    }
}
