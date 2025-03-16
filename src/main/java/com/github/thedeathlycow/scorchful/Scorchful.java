package com.github.thedeathlycow.scorchful;

import com.github.thedeathlycow.scorchful.api.ServerThirstPlugin;
import com.github.thedeathlycow.scorchful.block.NetherLilyBehaviours;
import com.github.thedeathlycow.scorchful.block.SandCauldronBehaviours;
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
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
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
    public static Identifier id(String path) {
        return Identifier.of(MODID, path);
    }

    @Override
    public void onInitialize() {
        AutoConfig.register(ScorchfulConfig.class, GsonConfigSerializer::new);
        configHolder = AutoConfig.getConfigHolder(ScorchfulConfig.class); //NOSONAR: this is correct usage for mods
        ScorchfulConfig.updateConfig(configHolder);

        SArmorMaterials.initialize();
        SBlocks.initialize();
        SBlockEntityTypes.initialize();
        SItems.initialize();
        SDataComponentTypes.initialize();
        SItemGroups.initialize();
        SEntityTypes.initialize();
        SSoundEvents.initialize();
        STemperatureEffects.initialize();
        SStatusEffects.initialize();
        SEntityAttributes.initialize();
        SParticleTypes.initialize();
        NetherBiomeModifications.initialize();
        SStats.initialize();
        SHeatVisions.initialize();
        SPotions.initialize();
        SEnvironmentProviderTypes.initialize();
        SandCauldronBehaviours.initialize();
        NetherLilyBehaviours.initialize();

        CommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess, environment) -> {
                    ThirstCommand.register(dispatcher);
                }
        );

        if (ScorchfulIntegrations.isDehydrationLoaded() && !ServerThirstPlugin.isCustomPluginLoaded()) {
            LOGGER.debug("Applying Dehydration thirst plugin");
            ServerThirstPlugin.registerPlugin(new DehydrationServerThirstPlugin());
        }

        this.registerThermooEventListeners();

        PayloadTypeRegistry.playS2C().register(TemperatureSoundEventPacket.PACKET_ID, TemperatureSoundEventPacket.PACKET_CODEC);

        LOGGER.info("Scorchful initialized!");

        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            var sb = new StringBuilder();
            Registries.BLOCK.streamEntries().forEach(block -> {
                sb.append("\n - ").append(block);
            });
            LOGGER.info("Blocks:{}", sb);
        });
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