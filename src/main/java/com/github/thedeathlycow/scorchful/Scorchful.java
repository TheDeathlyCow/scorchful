package com.github.thedeathlycow.scorchful;

import com.github.thedeathlycow.scorchful.block.NetherLilyBehaviours;
import com.github.thedeathlycow.scorchful.block.SandCauldronBehaviours;
import com.github.thedeathlycow.scorchful.config.ScorchfulConfig;
import com.github.thedeathlycow.scorchful.enchantment.RehydrationEnchantment;
import com.github.thedeathlycow.scorchful.event.ScorchfulLivingEntityEvents;
import com.github.thedeathlycow.scorchful.item.FireChargeThrower;
import com.github.thedeathlycow.scorchful.item.HeatResistantArmourTagApplicator;
import com.github.thedeathlycow.scorchful.registry.*;
import com.github.thedeathlycow.scorchful.server.ThirstCommand;
import com.github.thedeathlycow.scorchful.temperature.AmbientTemperatureController;
import com.github.thedeathlycow.scorchful.temperature.AttributeController;
import com.github.thedeathlycow.scorchful.temperature.WetTickController;
import com.github.thedeathlycow.scorchful.worldgen.NetherBiomeModifications;
import com.github.thedeathlycow.thermoo.api.temperature.HeatingModes;
import com.github.thedeathlycow.thermoo.api.temperature.event.EnvironmentControllerInitializeEvent;
import com.github.thedeathlycow.thermoo.api.temperature.event.PlayerEnvironmentEvents;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.item.v1.ModifyItemAttributeModifiersCallback;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Scorchful implements ModInitializer {

    public static final String MODID = "scorchful";

    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    public static final int CONFIG_VERSION = 4;

    @Nullable
    private static ConfigHolder<ScorchfulConfig> configHolder = null;

    @Contract("_->new")
    public static Identifier id(String path) {
        return Identifier.of(MODID, path);
    }

    @Override
    public void onInitialize() {
        AutoConfig.register(ScorchfulConfig.class, GsonConfigSerializer::new);
        ScorchfulConfig.updateConfig(AutoConfig.getConfigHolder(ScorchfulConfig.class));

        SBlocks.registerBlocks();
        SBlockEntityTypes.registerAll();
        SArmorMaterials.initialize();
        SDataComponentTypes.initialize();
        SItems.registerItems();
        SItemGroups.registerAll();
        SEntityTypes.registerAll();
        SSoundEvents.registerAll();
        STemperatureEffects.registerAll();
        SEnchantments.registerAll();
        SStatusEffects.initialize();
        RehydrationEnchantment.addToNetherLoot();
        SParticleTypes.registerAll();
        NetherBiomeModifications.placeFeaturesInBiomes();
        SStats.registerAll();
        SandCauldronBehaviours.registerAll();
        NetherLilyBehaviours.registerBehaviours();
        SHeatVisions.initialize();

        CommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess, environment) -> {
                    ThirstCommand.register(dispatcher);
                }
        );
        UseItemCallback.EVENT.register(new FireChargeThrower());
        ModifyItemAttributeModifiersCallback.EVENT.register(new HeatResistantArmourTagApplicator());

        // custom scorchful event
        ScorchfulLivingEntityEvents.ON_DAMAGED.register(
                (entity, source, amount) -> {
                    if (source.isOf(DamageTypes.FIREBALL) || source.isOf(DamageTypes.UNATTRIBUTED_FIREBALL)) {
                        entity.thermoo$addTemperature(
                                Scorchful.getConfig().heatingConfig.getFireballHeat(),
                                HeatingModes.ACTIVE
                        );
                    }
                }
        );

        this.registerThermooEventListeners();

        LOGGER.info("Scorchful initialized!");
    }

    @NotNull
    public static ScorchfulConfig getConfig() {
        if (configHolder == null) {
            configHolder = AutoConfig.getConfigHolder(ScorchfulConfig.class);
        }
        return configHolder.getConfig();
    }


    private void registerThermooEventListeners() {
        PlayerEnvironmentEvents.CAN_APPLY_PASSIVE_TEMPERATURE_CHANGE.register(
                (change, player) -> {
                    if (change <= 0) {
                        return TriState.DEFAULT;
                    }

                    ScorchfulConfig config = getConfig();

                    if (!config.heatingConfig.doPassiveHeating()) {
                        return TriState.FALSE;
                    } else {
                        return TriState.of(
                                player.thermoo$getTemperatureScale() < config.heatingConfig.getMaxPassiveHeatingScale()
                        );
                    }
                }
        );

        EnvironmentControllerInitializeEvent.EVENT.register(AttributeController::new);
        EnvironmentControllerInitializeEvent.EVENT.register(AmbientTemperatureController::new);
        EnvironmentControllerInitializeEvent.EVENT.register(WetTickController::new);
    }
}