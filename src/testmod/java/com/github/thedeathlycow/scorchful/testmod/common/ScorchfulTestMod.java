package com.github.thedeathlycow.scorchful.testmod.common;

import com.github.thedeathlycow.scorchful.Scorchful;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class ScorchfulTestMod implements ModInitializer {
    public static final String MODID = Scorchful.MODID + "-test";

    @Override
    public void onInitialize() {
    }

    public static Identifier id(String path) {
        return Identifier.of(MODID, path);
    }
}