package com.github.thedeathlycow.scorchful.temperature;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.compat.ScorchfulIntegrations;
import com.github.thedeathlycow.scorchful.components.PlayerComponent;
import com.github.thedeathlycow.scorchful.components.ScorchfulComponents;
import com.github.thedeathlycow.scorchful.config.ScorchfulConfig;
import com.github.thedeathlycow.scorchful.enchantment.SEnchantmentHelper;
import com.github.thedeathlycow.thermoo.api.temperature.EnvironmentController;
import com.github.thedeathlycow.thermoo.api.temperature.EnvironmentControllerDecorator;
import com.github.thedeathlycow.thermoo.api.temperature.Soakable;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.world.LightType;

public class WetTickController extends EnvironmentControllerDecorator {

    public WetTickController(EnvironmentController controller) {
        super(controller);
    }

    @Override
    public int getSoakChange(Soakable soakable) {

        if (!(soakable instanceof LivingEntity entity)) {
            return 0;
        }

        ScorchfulConfig config = Scorchful.getConfig();
        int soakChange = getWetChange(entity, config);
        soakChange = getDryChange(entity, config, soakChange);

        return soakChange;
    }

    private static int getWetChange(LivingEntity entity, ScorchfulConfig config) {
        int soakChange = 0;

        // immediately soak players in water
        if (entity.isSubmergedIn(FluidTags.WATER)) {
            return entity.thermoo$getMaxWetTicks();
        }

        // add wetness when touching, but not submerged in, water or rain
        if (entity.isTouchingWaterOrRain() || entity.getBlockStateAtPos().isOf(Blocks.WATER_CAULDRON)) {
            soakChange += config.thirstConfig.getTouchingWaterWetnessIncrease();
        }

        return soakChange;
    }

    private static int getDryChange(LivingEntity entity, ScorchfulConfig config, int wetChange) {
        // dry off slowly when not being wetted
        if (wetChange <= 0 && entity.thermoo$isWet()) {
            wetChange = -config.thirstConfig.getDryRate();
        }

        if (entity.isOnFire()) {
            wetChange -= config.thirstConfig.getOnFireDryDate();
        }

        if (entity instanceof PlayerEntity player) {
            tickRehydration(player, config, wetChange);
        }

        return wetChange;
    }

    private static void tickRehydration(PlayerEntity player, ScorchfulConfig config, int wetChange) {
        int rehydrationLevel = SEnchantmentHelper.getTotalRehydrationForPlayer(player);
        PlayerComponent component = ScorchfulComponents.PLAYER.get(player);
        if (rehydrationLevel > 0) {
            boolean dehydrationLoaded = ScorchfulIntegrations.isDehydrationLoaded();
            if (wetChange < 0 && player.getRandom().nextBoolean()) {
                component.tickRehydrationWaterRecapture(config, dehydrationLoaded);
            }
            component.tickRehydration(config, rehydrationLevel, dehydrationLoaded);
        } else {
            component.resetRehydration();
        }
    }

}
