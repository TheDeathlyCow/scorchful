package com.github.thedeathlycow.scorchful.compat;

import dev.yumi.mc.core.api.YumiMods;

public class ScorchfulIntegrations {

    public static final String FROSTIFUL_ID = "frostiful";

    public static final String ACCESSORIES_ID = "accessories";

    public static final String THIRST_WAS_TAKEN_ID = "thirst";

    public static final String NATURES_SPIRIT_ID = "natures_spirit";

    public static boolean isThirstWasTakenLoaded() {
        return isModLoaded(THIRST_WAS_TAKEN_ID);
    }

    public static boolean isAccessoriesLoaded() {
        return isModLoaded(ACCESSORIES_ID);
    }

    public static boolean isModLoaded(String id) {
        return YumiMods.get().isModLoaded(id);
    }

    private ScorchfulIntegrations() {

    }
}
