package com.github.thedeathlycow.scorchful;

import com.github.thedeathlycow.scorchful.api.ServerThirstPlugin;
import com.github.thedeathlycow.scorchful.block.NetherLilyBehaviours;
import com.github.thedeathlycow.scorchful.block.SandCauldronBehaviours;
import com.github.thedeathlycow.scorchful.compat.DehydrationServerThirstPlugin;
import com.github.thedeathlycow.scorchful.compat.ScorchfulIntegrations;
import com.github.thedeathlycow.scorchful.config.ScorchfulConfig;
import com.github.thedeathlycow.scorchful.event.ScorchfulItemEvents;
import com.github.thedeathlycow.scorchful.event.ScorchfulLivingEntityEvents;
import com.github.thedeathlycow.scorchful.item.DrinkItem;
import com.github.thedeathlycow.scorchful.item.FireChargeThrower;
import com.github.thedeathlycow.scorchful.item.HeatResistanceHelper;
import com.github.thedeathlycow.scorchful.item.component.DrinkLevelComponent;
import com.github.thedeathlycow.scorchful.item.enchantment.EnchantmentModifiers;
import com.github.thedeathlycow.scorchful.item.loot.TurtleScuteLootTableModifier;
import com.github.thedeathlycow.scorchful.registry.*;
import com.github.thedeathlycow.scorchful.registry.tag.SDamageTypeTags;
import com.github.thedeathlycow.scorchful.registry.tag.SItemTags;
import com.github.thedeathlycow.scorchful.server.ThirstCommand;
import com.github.thedeathlycow.scorchful.server.network.TemperatureSoundEventPacket;
import com.github.thedeathlycow.scorchful.temperature.AmbientTemperatureController;
import com.github.thedeathlycow.scorchful.temperature.AttributeController;
import com.github.thedeathlycow.scorchful.temperature.WetTickController;
import com.github.thedeathlycow.scorchful.worldgen.NetherBiomeModifications;
import com.github.thedeathlycow.thermoo.api.armor.material.ArmorMaterialEvents;
import com.github.thedeathlycow.thermoo.api.temperature.HeatingModes;
import com.github.thedeathlycow.thermoo.api.temperature.event.EnvironmentControllerInitializeEvent;
import com.github.thedeathlycow.thermoo.api.temperature.event.PlayerEnvironmentEvents;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Scorchful implements ModInitializer {

    public static final String MODID = "scorchful";

    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    public static final int CONFIG_VERSION = 5;

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

        SBlocks.registerBlocks();
        SBlockEntityTypes.registerAll();
        SArmorMaterials.initialize();
        SDataComponentTypes.initialize();
        SItems.registerItems();
        SItemGroups.registerAll();
        SEntityTypes.registerAll();
        SSoundEvents.registerAll();
        STemperatureEffects.registerAll();
        SStatusEffects.initialize();
        SEntityAttributes.initialize();
        SParticleTypes.registerAll();
        NetherBiomeModifications.placeFeaturesInBiomes();
        SStats.registerAll();
        SandCauldronBehaviours.registerAll();
        NetherLilyBehaviours.registerBehaviours();
        SHeatVisions.initialize();
        SPotions.initialize();

        CommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess, environment) -> {
                    ThirstCommand.register(dispatcher);
                }
        );

        if (ScorchfulIntegrations.isDehydrationLoaded() && !ServerThirstPlugin.isCustomPluginLoaded()) {
            ServerThirstPlugin.registerPlugin(new DehydrationServerThirstPlugin());
        }


        UseItemCallback.EVENT.register(new FireChargeThrower());
        ScorchfulItemEvents.GET_DEFAULT_STACK.register(DrinkLevelComponent::applyToNewStack);
        ScorchfulItemEvents.CONSUME_ITEM.register(DrinkItem::applyWater);
        ScorchfulItemEvents.CONSUME_ITEM.register((stack, player) -> {
            if (stack.isIn(SItemTags.IS_COOLING_FOOD)) {
                player.thermoo$addTemperature(
                        getConfig().heatingConfig.getTemperatureFromCoolingFood(),
                        HeatingModes.ACTIVE
                );
            }
        });
        ArmorMaterialEvents.GET_HEAT_RESISTANCE.register(HeatResistanceHelper::getHeatResistance);

        // custom scorchful event
        ServerLivingEntityEvents.AFTER_DAMAGE.register(
                (entity, source, baseDamageTaken, damageTaken, blocked) -> {
                    if (!blocked && source.isIn(SDamageTypeTags.FIREBALL)) {
                        entity.thermoo$addTemperature(
                                getConfig().heatingConfig.getFireballHeat(),
                                HeatingModes.ACTIVE
                        );
                    }
                }
        );

        this.registerThermooEventListeners();
        LootTableEvents.MODIFY.register(new TurtleScuteLootTableModifier());
        EnchantmentModifiers.initialize();
        
        PayloadTypeRegistry.playS2C().register(TemperatureSoundEventPacket.PACKET_ID, TemperatureSoundEventPacket.PACKET_CODEC);

        LOGGER.info("Scorchful initialized!");
    }

    @NotNull
    public static ScorchfulConfig getConfig() {
        return configHolder.getConfig();
    }


    private void registerThermooEventListeners() {
        PlayerEnvironmentEvents.CAN_APPLY_PASSIVE_TEMPERATURE_CHANGE.register(
                (change, player) -> {
                    if (change <= 0) {
                        return TriState.DEFAULT;
                    }

                    ScorchfulConfig config = getConfig();

                    int tickInterval = config.heatingConfig.getPassiveHeatingTickInterval();
                    if (tickInterval > 1 && player.age % tickInterval != 0) {
                        return TriState.FALSE;
                    }

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