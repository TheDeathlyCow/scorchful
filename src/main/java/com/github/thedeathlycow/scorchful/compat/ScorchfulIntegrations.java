package com.github.thedeathlycow.scorchful.compat;

import net.fabricmc.loader.api.FabricLoader;

public class ScorchfulIntegrations {

    public static final String FROSTIFUL_ID = "frostiful";

    public static final String TRINKETS_ID = "trinkets";

    public static final String COLORFUL_HEARTS_ID = "colorfulhearts";

    public static final String OVERFLOWING_BARS_ID = "overflowingbars";

    public static final String DEHYDRATION_ID = "dehydration";

    public static final String FABRIC_SEASONS_ID = "seasons";

    public static boolean isHeartsRenderOverridden() {
        return isModLoaded(COLORFUL_HEARTS_ID) || isModLoaded(OVERFLOWING_BARS_ID);
    }

    public static boolean isDehydrationLoaded() {
        return isModLoaded(DEHYDRATION_ID);
    }

    public static boolean isModLoaded(String id) {
        return FabricLoader.getInstance().isModLoaded(id);
    }

    private ScorchfulIntegrations() {

    }
}
