package com.github.thedeathlycow.scorchful.datagen;

import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ScorchfulDataGenerator {
    public static final String MODID = "scorchful-datagen";

    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    public static ResourceLocation commonID(String path) {
        return ResourceLocation.fromNamespaceAndPath("c", path);
    }

    private ScorchfulDataGenerator() {

    }
}