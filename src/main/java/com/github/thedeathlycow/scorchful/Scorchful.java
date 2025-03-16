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
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
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

        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
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

        if (ScorchfulIntegrations.isDehydrationLoaded() && !ServerThirstPlugin.isCustomPluginLoaded()) {
            LOGGER.debug("Applying Dehydration thirst plugin");
            ServerThirstPlugin.registerPlugin(new DehydrationServerThirstPlugin());
        }

        this.registerThermooEventListeners();

        PayloadTypeRegistry.playS2C().register(TemperatureSoundEventPacket.PACKET_ID, TemperatureSoundEventPacket.PACKET_CODEC);

        LOGGER.info("Scorchful initialized!");

        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            var blockupdate = new BlockUpdateS2CPacket(BlockPos.ORIGIN, SBlocks.SAND_CAULDRON.getDefaultState());
            try {
                var ids = Block.STATE_IDS;
                LOGGER.info("raw id: {}", ids.getRawId(SBlocks.SAND_CAULDRON.getDefaultState()));
                var factory = RegistryByteBuf.makeFactory(server.getRegistryManager());
                BlockUpdateS2CPacket.CODEC.encode(factory.apply(PacketByteBufs.create()), blockupdate);
            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.info("exception: {}", e);
            }
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