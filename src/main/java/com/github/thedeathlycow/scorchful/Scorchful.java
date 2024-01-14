package com.github.thedeathlycow.scorchful;

import com.github.thedeathlycow.scorchful.config.ScorchfulConfig;
import com.github.thedeathlycow.scorchful.event.ScorchfulLivingEntityEvents;
import com.github.thedeathlycow.scorchful.registry.SItemGroups;
import com.github.thedeathlycow.scorchful.registry.SItems;
import com.github.thedeathlycow.scorchful.registry.SSoundEvents;
import com.github.thedeathlycow.scorchful.server.ThirstCommand;
import com.github.thedeathlycow.scorchful.temperature.AmbientTemperatureController;
import com.github.thedeathlycow.scorchful.temperature.AttributeController;
import com.github.thedeathlycow.scorchful.temperature.PlayerAttributeController;
import com.github.thedeathlycow.scorchful.temperature.WetTickController;
import com.github.thedeathlycow.thermoo.api.temperature.HeatingModes;
import com.github.thedeathlycow.thermoo.api.temperature.event.EnvironmentControllerInitializeEvent;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Contract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Scorchful implements ModInitializer {

    public static final String MODID = "scorchful";

    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    public static final int CONFIG_VERSION = 2;

    @Contract("_->new")
    public static Identifier id(String path) {
        return new Identifier(MODID, path);
    }

    @Override
    public void onInitialize() {
        AutoConfig.register(ScorchfulConfig.class, GsonConfigSerializer::new);
        ScorchfulConfig.updateConfig(AutoConfig.getConfigHolder(ScorchfulConfig.class));

        SItems.registerItems();
        SItemGroups.registerAll();
        SSoundEvents.registerAll();

        CommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess, environment) -> {
                    ThirstCommand.register(dispatcher);
                }
        );

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

    public static ScorchfulConfig getConfig() {
        return AutoConfig.getConfigHolder(ScorchfulConfig.class).getConfig();
    }


    private void registerThermooEventListeners() {
        EnvironmentControllerInitializeEvent.EVENT.register(AttributeController::new);
        EnvironmentControllerInitializeEvent.EVENT.register(
                EnvironmentControllerInitializeEvent.MODIFY_PHASE,
                PlayerAttributeController::new
        );

        EnvironmentControllerInitializeEvent.EVENT.register(AmbientTemperatureController::new);
        EnvironmentControllerInitializeEvent.EVENT.register(WetTickController::new);
    }
}