package com.github.thedeathlycow.scorchful.api;

import com.github.thedeathlycow.scorchful.temperature.ServerThirstPluginManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public interface ServerThirstPlugin {
    void onConsumeDrink(ServerPlayerEntity player, ItemStack stack);

    int tickSweatTransfer(PlayerEntity player);

    int getRehydrationEnchantmentDrinkSize();

    boolean rehydrateFromEnchantment(double rehydrationEfficiency);

    static void registerPlugin(@NotNull ServerThirstPlugin plugin) {
        Objects.requireNonNull(plugin, "Thirst plugin may not be null!");
        ServerThirstPluginManager.getInstance().registerPlugin(plugin);
    }

    @NotNull
    static ServerThirstPlugin getActivePlugin() {
        return ServerThirstPluginManager.getInstance().getPlugin();
    }
}