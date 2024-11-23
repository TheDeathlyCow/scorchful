package com.github.thedeathlycow.scorchful.api;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.compat.ScorchfulServerThirstPlugin;
import com.github.thedeathlycow.scorchful.compat.ServerThirstPluginManager;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * An adapter for delegating thirst-based interactions in Scorchful to an alternative thirst system on the logical server.
 * <p>
 * By default, Scorchful uses its own thirst system, managed by {@link ScorchfulServerThirstPlugin}. However, custom
 * implementations of this interface can be registered to replace the default behavior.
 * <p>
 * Scorchful will provide a built-in custom plugin for <a href="https://www.modrinth.com/mod/dehydration">Dehydration</a>
 * if that mod is detected.
 *
 * @see com.github.thedeathlycow.scorchful.compat.DehydrationServerThirstPlugin
 * @see ScorchfulServerThirstPlugin
 */
public interface ServerThirstPlugin {
    /**
     * Attempts to dehydrate the player from sweating.
     *
     * @param player The player to dehydrate
     * @return Returns {@code true} if the player was successfully dehydrated and can have that water added to their
     * {@linkplain com.github.thedeathlycow.thermoo.api.temperature.Soakable soaked ticks}.
     */
    boolean dehydrateFromSweating(PlayerEntity player);

    /**
     * Rehydrates the player from {@linkplain com.github.thedeathlycow.scorchful.components.RehydrationComponent Rehydration}.
     * <p>
     * Rehydration is usually provided as an {@link net.minecraft.enchantment.Enchantment}, but is internally based on an
     * {@linkplain com.github.thedeathlycow.scorchful.registry.SEntityAttributes#REHYDRATION_EFFICIENCY attribute}.
     *
     * @param player                The player to rehydrate
     * @param waterCaptured         The water that Rehydration has captured, as {@linkplain com.github.thedeathlycow.thermoo.api.temperature.Soakable soaked ticks}
     * @param rehydrationEfficiency How much of the water recaptured should be converted to thirst water, as a percentage
     *                              between 0 and 1.
     */
    void rehydrateFromEnchantment(PlayerEntity player, int waterCaptured, double rehydrationEfficiency);

    /**
     * Gets the amount of water that the Rehydration Enchantment must recapture before the player can drink it, as
     * {@linkplain com.github.thedeathlycow.thermoo.api.temperature.Soakable soaked ticks}
     *
     * @return Returns the size of the rehydration drink captured, in soaked ticks.
     */
    default int getRehydrationThreshold() {
        return Scorchful.getConfig().thirstConfig.getRehydrationDrinkSize();
    }

    /**
     * Registers a non-null custom thirst plugin. Only one custom plugin may be registered. If a custom plugin is
     * already registered, then an {@link IllegalStateException} will be thrown.
     *
     * @param plugin The custom thirst plugin to register
     * @throws NullPointerException  If attempting to register a null plugin
     * @throws IllegalStateException If a custom plugin is already registered
     */
    static void registerPlugin(@NotNull ServerThirstPlugin plugin) {
        Objects.requireNonNull(plugin, "Thirst plugin may not be null!");
        ServerThirstPluginManager.getInstance().registerPlugin(plugin);
    }

    /**
     * Gets the current active thirst plugin.
     *
     * @return Returns the custom plugin if one is registered, otherwise returns the default {@link ScorchfulServerThirstPlugin}.
     */
    @NotNull
    static ServerThirstPlugin getActivePlugin() {
        return ServerThirstPluginManager.getInstance().getPlugin();
    }

    /**
     * Gets the default plugin behaviour.
     *
     * @return Returns a global instance of the default thirst plugin.
     */
    static ServerThirstPlugin getDefaultPlugin() {
        return ServerThirstPluginManager.DEFAULT;
    }

    /**
     * @return Returns {@code true} if a custom plugin is already loaded
     */
    static boolean isCustomPluginLoaded() {
        return ServerThirstPluginManager.getInstance().isCustomPluginLoaded();
    }
}