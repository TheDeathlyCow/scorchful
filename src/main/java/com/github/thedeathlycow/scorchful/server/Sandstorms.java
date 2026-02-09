package com.github.thedeathlycow.scorchful.server;

import com.github.thedeathlycow.scorchful.registry.tag.SBiomeTags;
import com.mojang.serialization.Codec;
import com.thedeathlycow.immersive.storms.util.WeatherEffectType;
import com.thedeathlycow.immersive.storms.util.WeatherEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

public class Sandstorms {

    public enum SandstormType implements StringRepresentable {
        NONE("no_sandstorm"),
        REGULAR("regular_sandstorm"),
        RED("red_sandstorm");

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
     * @return Returns {@link SandstormType#NONE} if it is not sand storming at the position in the world.
     * Returns {@link SandstormType#REGULAR} if it is raining in a desert and {@link SandstormType#RED} if it is raining
     * in a badlands.
     */
    public static SandstormType getCurrentSandStorm(Level world, BlockPos pos, boolean includeSurface) {
        WeatherEffectType type = WeatherEffects.getCurrentType(world, pos, includeSurface);

        if (type == WeatherEffectType.SANDSTORM) {
            Holder<Biome> biome = world.getBiomeManager().getNoiseBiomeAtPosition(pos);
            if (hasRedSandStorms(biome)) {
                return SandstormType.RED;
            } else if (hasRegularSandStorms(biome)) {
                return SandstormType.REGULAR;
            }
        }

        return SandstormType.NONE;
    }

    public static SandstormType getCurrentSandStorm(Level world, BlockPos pos) {
        return getCurrentSandStorm(world, pos, true);
    }

    public static boolean hasRegularSandStorms(Holder<Biome> biome) {
        return !biome.value().hasPrecipitation() && biome.is(SBiomeTags.HAS_REGULAR_SAND_STORMS);
    }

    public static boolean hasRedSandStorms(Holder<Biome> biome) {
        return !biome.value().hasPrecipitation() && biome.is(SBiomeTags.HAS_RED_SAND_STORMS);
    }

    private Sandstorms() {

    }
}
