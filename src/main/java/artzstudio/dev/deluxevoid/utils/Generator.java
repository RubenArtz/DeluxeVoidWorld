/*
 *
 *  Copyright (c) 2026 Ruben_Artz and Artz Studio. All rights reserved.
 *
 *  This code is proprietary software. It is strictly prohibited to
 *  copy, modify, distribute, or use this code for any purpose
 *  without the express written permission of the owner.
 *
 *  Project: Deluxe Void World
 *
 */

package artzstudio.dev.deluxevoid.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

import java.util.Collections;
import java.util.List;
import java.util.Random;

@SuppressWarnings({"NullableProblems", "deprecation"})
public class Generator extends ChunkGenerator {

    public Location getFixedSpawnLocation(World world, Random random) {
        return new Location(world, 0.0D, 80.0D, 0.0D);
    }

    public List<BlockPopulator> getDefaultPopulators(World world) {
        return Collections.emptyList();
    }

    public ChunkGenerator.ChunkData generateChunkData(World world, Random random, int ChunkX, int ChunkZ, ChunkGenerator.BiomeGrid biome) {
        ChunkGenerator.ChunkData data = createChunkData(world);
        if (0 >= ChunkX << 4 && 0 < ChunkX + 1 << 4 && 0 >= ChunkZ << 4 && 0 < ChunkZ + 1 << 4) {
            data.setBlock(0, 80, 0, Material.BEDROCK);
        }
        return data;
    }
}