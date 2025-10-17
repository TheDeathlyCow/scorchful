package com.github.thedeathlycow.scorchful;

import com.github.thedeathlycow.scorchful.api.ServerThirstPlugin;
import com.github.thedeathlycow.scorchful.compat.DehydrationServerThirstPlugin;
import com.github.thedeathlycow.scorchful.compat.ScorchfulIntegrations;
import com.github.thedeathlycow.scorchful.config.ScorchfulConfig;
import com.github.thedeathlycow.scorchful.registry.*;
import com.github.thedeathlycow.scorchful.server.ThirstCommand;
import com.github.thedeathlycow.scorchful.server.network.TemperatureSoundEventPacket;
import com.github.thedeathlycow.scorchful.temperature.ActiveTemperatureEffects;
import com.github.thedeathlycow.scorchful.temperature.PassiveTemperatureEffects;
import com.github.thedeathlycow.scorchful.temperature.ServerPlayerEnvironmentTickListeners;
import com.github.thedeathlycow.scorchful.temperature.SoakingEffects;
import com.github.thedeathlycow.scorchful.worldgen.NetherBiomeModifications;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Contract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class Scorchful implements ModInitializer {

    public static final String MODID = "scorchful";

    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    @Contract("_->new")
    public static Identifier id(String path) {
        return Identifier.of(MODID, path);
    }

    @Override
    public void onInitialize() {
        ScorchfulConfig.initialize();

        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            CommandRegistrationCallback.EVENT.register(
                    (dispatcher, registryAccess, environment) -> {
                        ThirstCommand.register(dispatcher);
                    }
            );
        }

        SBlocks.initialize();
        SDataComponentTypes.initialize();
        SItems.initialize();
        SEntityTypes.initialize();
        SPotions.initialize();
        SItemGroups.initialize();
        SSoundEvents.initialize();
        STemperatureEffects.initialize();
        SStatusEffects.initialize();
        SParticleTypes.initialize();
        NetherBiomeModifications.initialize();
        SStats.initialize();
        SHeatVisions.initialize();
        SEnvironmentProviderTypes.initialize();
        SEntityAttributes.initialize();
        SPointsOfInterest.initialize();


        if (ScorchfulIntegrations.isDehydrationLoaded() && !ServerThirstPlugin.isCustomPluginLoaded()) {
            LOGGER.debug("Applying Dehydration thirst plugin");
            ServerThirstPlugin.registerPlugin(new DehydrationServerThirstPlugin());
        }

        this.registerThermooEventListeners();

        PayloadTypeRegistry.playS2C().register(TemperatureSoundEventPacket.PACKET_ID, TemperatureSoundEventPacket.PACKET_CODEC);

        LOGGER.info("Scorchful initialized!");
    }

    public static Path getConfigDir() {
        return FabricLoader.getInstance().getConfigDir().resolve(MODID);
    }

    private void registerThermooEventListeners() {
        ServerPlayerEnvironmentTickListeners.initialize();
        ActiveTemperatureEffects.initialize();
        PassiveTemperatureEffects.initialize();
        SoakingEffects.initialize();
    }
}