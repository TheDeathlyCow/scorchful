package com.github.thedeathlycow.scorchful.registry.tag;

import com.github.thedeathlycow.scorchful.Scorchful;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class SBlockTags {

    public static final TagKey<Block> HEAVY_ICE = of("heavy_ice");

    public static final TagKey<Block> NETHER_LILY_CAN_ABSORB_WATER = of("nether_lily_can_absorb_water");

    public static final TagKey<Block> SAND_PILE_CAN_SURVIVE_ON = of("sand_pile_can_survive_on");

    public static final TagKey<Block> SAND_PILE_CANNOT_SURVIVE_ON = of("sand_pile_cannot_survive_on");

    private static TagKey<Block> of(String path) {
        return TagKey.create(Registries.BLOCK, Scorchful.id(path));
    }

}
