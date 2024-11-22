package com.github.thedeathlycow.scorchful.temperature;

import com.github.thedeathlycow.scorchful.api.ServerThirstPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ServerThirstPluginManager {

    private static final ServerThirstPluginManager INSTANCE = new ServerThirstPluginManager();

    @Nullable
    private ServerThirstPlugin customPlugin = null;

    public static ServerThirstPluginManager getInstance() {
        return INSTANCE;
    }

    @NotNull
    public ServerThirstPlugin getPlugin() {
        return customPlugin;
    }

    public void registerPlugin(ServerThirstPlugin plugin) {
        if (this.customPlugin != null) {
            throw new IllegalStateException("Thirst plugin already registered!");
        }

        this.customPlugin = plugin;
    }

    private ServerThirstPluginManager() {

    }
}