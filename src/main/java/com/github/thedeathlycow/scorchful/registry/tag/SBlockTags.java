package com.github.thedeathlycow.scorchful.registry.tag;

import com.github.thedeathlycow.scorchful.Scorchful;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class SBlockTags {

    public static final TagKey<Block> HEAVY_ICE = of("heavy_ice");

    private static TagKey<Block> of(String path) {
        return TagKey.of(RegistryKeys.BLOCK, Scorchful.id(path));
    }

}
