package com.github.thedeathlycow.scorchful;

import com.github.thedeathlycow.scorchful.api.ServerThirstPlugin;
import com.github.thedeathlycow.scorchful.compat.ScorchfulIntegrations;
import com.github.thedeathlycow.scorchful.compat.ThirstWasTakenPlugin;
import com.github.thedeathlycow.scorchful.config.ScorchfulConfig;
import com.github.thedeathlycow.scorchful.registry.*;
import com.github.thedeathlycow.scorchful.server.ThirstCommand;
import com.github.thedeathlycow.scorchful.server.network.TemperatureSoundEventPacket;
import com.github.thedeathlycow.scorchful.temperature.ActiveTemperatureEffects;
import com.github.thedeathlycow.scorchful.temperature.PassiveTemperatureEffects;
import com.github.thedeathlycow.scorchful.temperature.ServerPlayerEnvironmentTickListeners;
import com.github.thedeathlycow.scorchful.temperature.SoakingEffects;
import com.github.thedeathlycow.scorchful.worldgen.NetherBiomeModifications;
import dev.yumi.mc.core.api.ModContainer;
import dev.yumi.mc.core.api.YumiMods;
import dev.yumi.mc.core.api.entrypoint.ModInitializer;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Scorchful implements ModInitializer {

    public static final String MODID = "scorchful";

    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    public static final int CONFIG_VERSION = 6;

    private static ConfigHolder<ScorchfulConfig> configHolder = null;

    @Contract("_->new")
    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    @Override
    public void onInitialize(ModContainer mod) {
        AutoConfig.register(ScorchfulConfig.class, GsonConfigSerializer::new);
        configHolder = AutoConfig.getConfigHolder(ScorchfulConfig.class); //NOSONAR: this is correct usage for mods
        ScorchfulConfig.updateConfig(configHolder);

        if (YumiMods.get().isDevelopmentEnvironment()) {
            CommandRegistrationCallback.EVENT.register(
                    (dispatcher, registryAccess, environment) -> {
                        ThirstCommand.register(dispatcher);
                    }
            );
        }

        SArmorMaterials.initialize();
        SBlocks.initialize();
        SBlockEntityTypes.initialize();
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

        if (ScorchfulIntegrations.isThirstWasTakenLoaded() && !ServerThirstPlugin.isCustomPluginLoaded()) {
            LOGGER.debug("Applying Thirst Was Taken thirst plugin");
            ServerThirstPlugin.registerPlugin(new ThirstWasTakenPlugin());
        }

        this.registerThermooEventListeners();

        PayloadTypeRegistry.playS2C().register(TemperatureSoundEventPacket.PACKET_ID, TemperatureSoundEventPacket.PACKET_CODEC);

        LOGGER.info("Scorchful initialized!");
    }

    @NotNull
    public static ScorchfulConfig getConfig() {
        return configHolder.getConfig();
    }


    private void registerThermooEventListeners() {
        ServerPlayerEnvironmentTickListeners.initialize();
        ActiveTemperatureEffects.initialize();
        PassiveTemperatureEffects.initialize();
        SoakingEffects.initialize();
    }
}