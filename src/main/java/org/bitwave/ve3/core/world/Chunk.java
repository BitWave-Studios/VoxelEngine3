package org.bitwave.ve3.core.world;

/**
 * A chunk of blocks in the world. Each chunk is a 16x64x16 cube of blocks.
 * Chunks are used to efficiently manage and render the world.
 * @author k4i
 */
public class Chunk {
    public static final int SIZE = 16;
    public static final int HEIGHT = 64;

    private final byte[] blocks = new byte[SIZE * HEIGHT * SIZE];

    private int index(int x, int y, int z) {
        return x + SIZE * (z + HEIGHT * y);
    }

    public void setBlock(int x, int y, int z, byte id) {
        if (x < 0 || x >= SIZE || y < 0 || y >= HEIGHT || z < 0 || z >= SIZE) {
            throw new IllegalArgumentException("Chunk block out of bounds.");
        }
        blocks[index(x, y, z)] = id;
    }

    public byte getBlock(int x, int y, int z) {
        if (x < 0 || x >= SIZE || y < 0 || y >= HEIGHT || z < 0 || z >= SIZE) {
            return 0;
        }
        return blocks[index(x, y, z)];
    }

    public boolean isEmpty() {
        for (byte block : blocks) {
            if (block != 0) return false;
        }
        return true;
    }
}
