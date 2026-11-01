package org.bitwave.ve3.core.world;

import org.joml.Vector3f;

/**
 * Small voxel storage - uses bytes for block IDs and renderer-friendly API.
 */
public class World {
    private final int sizeX, height, sizeZ;
    private final byte[] blocks;

    public World(int sizeX, int height, int sizeZ) {
        this.sizeX = sizeX;
        this.height = height;
        this.sizeZ = sizeZ;
        this.blocks = new byte[sizeX * height * sizeZ];
    }

    private int index(int x, int y, int z) {
        return x + sizeX * (y + height * z);
    }

    public void setBlock(int x, int y, int z, byte id) {
        if (inBounds(x,y,z)) blocks[index(x,y,z)] = id;
    }

    public byte getBlock(int x, int y, int z) {
        if (!inBounds(x,y,z)) return 0;
        return blocks[index(x,y,z)];
    }

    public boolean inBounds(int x, int y, int z) {
        return x>=0 && x<sizeX && y>=0 && y<height && z>=0 && z<sizeZ;
    }

    public int getSizeX() { return sizeX; }
    public int getSizeZ() { return sizeZ; }
    public int getHeight() { return height; }

    public void generateFlatWorld() {
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

    public Vector3f getBlockColor(int id) {
        return switch (id) {
            case 1 -> new Vector3f(0.59f, 0.39f, 0.12f); // Dirt
            case 2 -> new Vector3f(0.13f, 0.55f, 0.13f); // Grass
            default -> new Vector3f(1f, 0f, 1f);
        };
    }
}
