package com.github.thedeathlycow.scorchful.api;

import com.github.thedeathlycow.thermoo.api.client.StatusBarOverlayRenderEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import org.jetbrains.annotations.Nullable;

public interface ClientThirstPlugin {
    @Nullable
    default ItemTooltipCallback tooltipCallback() {
        return null;
    }

    @Nullable
    default StatusBarOverlayRenderEvents.RenderHealthBarCallback heartOverlayCallback() {
        return null;
    }
}