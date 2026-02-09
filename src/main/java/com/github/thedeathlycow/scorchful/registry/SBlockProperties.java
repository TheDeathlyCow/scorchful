package com.github.thedeathlycow.scorchful.registry;

import net.minecraft.world.level.block.state.properties.IntegerProperty;

public final class SBlockProperties {
    public static final IntegerProperty WATER_LEVEL_0_3 = IntegerProperty.create("water_level", 0, 3);

    private SBlockProperties() {

    }
}