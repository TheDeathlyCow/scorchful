package com.github.thedeathlycow.scorchful.client.config;

import com.github.thedeathlycow.scorchful.client.config.section.AccessibilitySettings;
import com.github.thedeathlycow.scorchful.client.config.section.DisplaySettings;

public final class ScorchfulClientConfig {
    public static AccessibilitySettings getAccessibilitySettings() {
        return AccessibilitySettings.HANDLER.instance();
    }

    public static DisplaySettings getDisplaySettings() {
        return DisplaySettings.HANDLER.instance();
    }

    public static void initialize() {
        AccessibilitySettings.HANDLER.load();
        AccessibilitySettings.HANDLER.save();

        DisplaySettings.HANDLER.load();
        DisplaySettings.HANDLER.save();
    }

    private ScorchfulClientConfig() {

    }
}