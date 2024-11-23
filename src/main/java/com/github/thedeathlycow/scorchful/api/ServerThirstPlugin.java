package com.github.thedeathlycow.scorchful.api;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.compat.ServerThirstPluginManagerImpl;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public interface ServerThirstPlugin {

    boolean dehydrateFromSweating(PlayerEntity player);

    void rehydrateFromEnchantment(PlayerEntity player, int waterCaptured, double rehydrationEfficiency);

    default int getMaxRehydrationWaterCapacity() {
        return Scorchful.getConfig().thirstConfig.getRehydrationDrinkSize();
    }

    static void registerPlugin(@NotNull ServerThirstPlugin plugin) {
        Objects.requireNonNull(plugin, "Thirst plugin may not be null!");
        ServerThirstPluginManagerImpl.getInstance().registerPlugin(plugin);
    }

    @NotNull
    static ServerThirstPlugin getActivePlugin() {
        return ServerThirstPluginManagerImpl.getInstance().getPlugin();
    }

    static boolean isCustomPluginLoaded() {
        return ServerThirstPluginManagerImpl.getInstance().isCustomPluginLoaded();
    }
}