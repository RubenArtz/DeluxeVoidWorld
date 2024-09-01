package ruben_artz.world.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

import java.util.*;

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