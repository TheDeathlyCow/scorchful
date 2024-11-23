package com.github.thedeathlycow.scorchful.compat;

import com.github.thedeathlycow.scorchful.api.ServerThirstPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ServerThirstPluginManagerImpl {

    private static final ServerThirstPluginManagerImpl INSTANCE = new ServerThirstPluginManagerImpl();

    private static final ServerThirstPlugin DEFAULT = new ScorchfulServerThirstPlugin();

    @Nullable
    private ServerThirstPlugin customPlugin = null;

    public static ServerThirstPluginManagerImpl getInstance() {
        return INSTANCE;
    }

    @NotNull
    public ServerThirstPlugin getPlugin() {
        return this.customPlugin != null ? this.customPlugin : DEFAULT;
    }

    public boolean isCustomPluginLoaded() {
        return this.customPlugin != null;
    }

    public void registerPlugin(ServerThirstPlugin plugin) {
        if (this.customPlugin != null) {
            throw new IllegalStateException("Thirst plugin already registered!");
        }

        this.customPlugin = plugin;
    }

    private ServerThirstPluginManagerImpl() {

    }
}