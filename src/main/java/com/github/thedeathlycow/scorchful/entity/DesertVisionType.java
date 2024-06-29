package com.github.thedeathlycow.scorchful.entity;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.random.Random;

public enum DesertVisionType implements StringIdentifiable {

    DESERT_WELL("desert_well"),
    HUSK("husk"),
    POPPY("poppy"),
    BOAT("boat");

    public static final Codec<DesertVisionType> CODEC = StringIdentifiable.createCodec(DesertVisionType::values);

    private final String name;

    DesertVisionType(String name) {
        this.name = name;
    }

    @Override
    public String asString() {
        return this.name;
    }

    public static DesertVisionType choose(Random random) {
        int index = random.nextInt(values().length);
        return values()[index];
    }
}
