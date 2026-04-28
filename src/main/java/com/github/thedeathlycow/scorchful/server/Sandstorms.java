package com.github.thedeathlycow.scorchful.server;

import com.github.thedeathlycow.scorchful.registry.tag.SBiomeTags;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;

public class Sandstorms {

    public enum SandstormType implements StringRepresentable {
        NONE("no_sandstorm"),
        REGULAR("regular_sandstorm"),
        RED("red_sandstorm"),
        PINK("pink_sandstorm");

        public static final Codec<SandstormType> CODEC = StringRepresentable.fromEnum(SandstormType::values);

        private final String id;

        SandstormType(String id) {
            this.id = id;
        }

        @Override
        public String getSerializedName() {
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
    public static SandstormType getCurrentSandStorm(Level world, BlockPos pos, boolean includeSurface) {
        if (!world.isRaining() || (includeSurface && world.isRainingAt(pos))) {
            return SandstormType.NONE;
        }
        if (includeSurface && !world.canSeeSky(pos)) {
            return SandstormType.NONE;
        }
        if (includeSurface && world.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, pos).getY() > pos.getY()) {
            return SandstormType.NONE;
        }
        Holder<Biome> biome = world.getBiome(pos);
        if (hasRedSandStorms(biome)) {
            return SandstormType.RED;
        } else if (hasPinkSandStorms(biome)) {
            return SandstormType.PINK;
        } else if (hasRegularSandStorms(biome)) {
            return SandstormType.REGULAR;
        } else {
            return SandstormType.NONE;
        }
    }

    public static SandstormType getCurrentSandStorm(Level world, BlockPos pos) {
        return getCurrentSandStorm(world, pos, true);
    }

    public static boolean isSandStorming(Level world, BlockPos pos) {
        return getCurrentSandStorm(world, pos, false) != SandstormType.NONE;
    }

    /**
     * Determines if the given biome can have sandstorms. Does not determine if it is currently sand storming - just
     * that the possibility of sand storms exists in that biome.
     *
     * @param biome
     * @return
     */
    public static boolean hasSandStorms(Holder<Biome> biome) {
        return !biome.value().hasPrecipitation() && biome.is(SBiomeTags.HAS_SAND_STORMS);
    }


    public static boolean hasRegularSandStorms(Holder<Biome> biome) {
        return !biome.value().hasPrecipitation() && biome.is(SBiomeTags.HAS_REGULAR_SAND_STORMS);
    }

    public static boolean hasRedSandStorms(Holder<Biome> biome) {
        return !biome.value().hasPrecipitation() && biome.is(SBiomeTags.HAS_RED_SAND_STORMS);
    }

    public static boolean hasPinkSandStorms(Holder<Biome> biome) {
        return !biome.value().hasPrecipitation() && biome.is(SBiomeTags.HAS_PINK_SAND_STORMS);
    }

    private Sandstorms() {

    }
}
