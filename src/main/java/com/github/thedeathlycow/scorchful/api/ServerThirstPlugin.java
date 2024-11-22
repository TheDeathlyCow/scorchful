package com.github.thedeathlycow.scorchful.api;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.compat.ServerThirstPluginManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public interface ServerThirstPlugin {
    void onConsumeDrink(ServerPlayerEntity player, ItemStack stack);

    int tickSweatTransfer(PlayerEntity player);

    void rehydrateFromEnchantment(PlayerEntity player, int waterCaptured, double rehydrationEfficiency);

    default int getMaxRehydrationWaterCapacity() {
        return Scorchful.getConfig().getRehydrationDrinkSize(false);
    }

    static void registerPlugin(@NotNull ServerThirstPlugin plugin) {
        Objects.requireNonNull(plugin, "Thirst plugin may not be null!");
        ServerThirstPluginManager.getInstance().registerPlugin(plugin);
    }

    @NotNull
    static ServerThirstPlugin getActivePlugin() {
        return ServerThirstPluginManager.getInstance().getPlugin();
    }

    static boolean isCustomPluginLoaded() {
        return ServerThirstPluginManager.getInstance().isCustomPluginLoaded();
    }
}