package com.github.thedeathlycow.scorchful.server;

import com.github.thedeathlycow.scorchful.registry.tag.SBiomeTags;
import com.mojang.serialization.Codec;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class Sandstorms {

    public enum SandstormType implements StringIdentifiable {
        NONE("no_sandstorm"),
        REGULAR("regular_sandstorm"),
        RED("red_sandstorm");

        public static final Codec<SandstormType> CODEC = StringIdentifiable.createCodec(SandstormType::values);

        private final String id;

        SandstormType(String id) {
            this.id = id;
        }

        @Override
        public String asString() {
            return this.id;
        }
    }

    /**
     * Determines if the position in the world has an active sand storm.
     *
     * @param world
     * @param pos
     * @return Returns {@link SandstormType#NONE} if it is not sand storming at the position in the world.
     * Returns {@link SandstormType#REGULAR} if it is raining in a desert and {@link SandstormType#RED} if it is raining
     * in a badlands.
     */
    public static SandstormType getCurrentSandStorm(World world, BlockPos pos, boolean includeSurface) {
        if (!world.isRaining() || (includeSurface && world.hasRain(pos))) {
            return SandstormType.NONE;
        }
        if (includeSurface && !world.isSkyVisible(pos)) {
            return SandstormType.NONE;
        }
        if (includeSurface && world.getTopPosition(Heightmap.Type.MOTION_BLOCKING, pos).getY() > pos.getY()) {
            return SandstormType.NONE;
        }
        RegistryEntry<Biome> biome = world.getBiome(pos);
        if (hasRegularSandStorms(biome)) {
            return SandstormType.REGULAR;
        } else if (hasRedSandStorms(biome)) {
            return SandstormType.RED;
        } else {
            return SandstormType.NONE;
        }
    }

    public static SandstormType getCurrentSandStorm(World world, BlockPos pos) {
        return getCurrentSandStorm(world, pos, true);
    }

    public static boolean isSandStorming(World world, BlockPos pos) {
        return getCurrentSandStorm(world, pos, false) != SandstormType.NONE;
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
