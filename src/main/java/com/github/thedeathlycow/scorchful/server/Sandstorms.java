package com.github.thedeathlycow.scorchful.server;

import com.github.thedeathlycow.scorchful.registry.tag.SBiomeTags;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class Sandstorms {

    public static boolean isSandStorming(World world, BlockPos pos) {
        if (!world.isRaining()) {
            return false;
        }
        if (!world.isSkyVisible(pos)) {
            return false;
        }
        if (world.getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, pos).getY() > pos.getY()) {
            return false;
        }
        RegistryEntry<Biome> biome = world.getBiome(pos);
        return hasSandStorms(biome);
    }

    /**
     * Determines if the given biome can have sandstorms. Does not determine if it is currently sand storming - just
     * that the possibility of sand storms exists in that biome.
     *
     * @param biome
     * @return
     */
    public static boolean hasSandStorms(RegistryEntry<Biome> biome) {
        return !biome.value().hasPrecipitation() && biome.isIn(SBiomeTags.HAS_SAND_STORMS);
    }


    public static boolean hasRegularSandStorms(RegistryEntry<Biome> biome) {
        return !biome.value().hasPrecipitation() && biome.isIn(SBiomeTags.HAS_REGULAR_SAND_STORMS);
    }

    public static boolean hasRedSandStorms(RegistryEntry<Biome> biome) {
        return !biome.value().hasPrecipitation() && biome.isIn(SBiomeTags.HAS_RED_SAND_STORMS);
    }


    private Sandstorms() {

    }
}
