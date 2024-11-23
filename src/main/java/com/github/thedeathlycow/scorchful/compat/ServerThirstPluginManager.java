package com.github.thedeathlycow.scorchful.compat;

import com.github.thedeathlycow.scorchful.api.ServerThirstPlugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
public final class ServerThirstPluginManager {

    public static final ServerThirstPlugin DEFAULT = new ScorchfulServerThirstPlugin();

    private static final ServerThirstPluginManager INSTANCE = new ServerThirstPluginManager();

    @Nullable
    private ServerThirstPlugin customPlugin = null;

    public static ServerThirstPluginManager getInstance() {
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

    private ServerThirstPluginManager() {

    }
}