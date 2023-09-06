package com.github.thedeathlycow.scorchful.compat;

import net.fabricmc.loader.api.FabricLoader;

public class ScorchfulIntegrations {

    public static final String FROSTIFUL_ID = "frostiful";


    public static boolean isModLoaded(String id) {
        return FabricLoader.getInstance().isModLoaded(id);
    }
}
