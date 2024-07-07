package com.github.thedeathlycow.scorchful.temperature.desertvision;

import com.github.thedeathlycow.scorchful.registry.SDesertVisionControllers;

public class VisionGenerator {

    public DesertVisionController chooseVision() {
        return SDesertVisionControllers.BOAT;
    }

}
