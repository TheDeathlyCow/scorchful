package com.github.thedeathlycow.scorchful.temperature;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.components.ScorchfulComponents;
import com.github.thedeathlycow.scorchful.config.ScorchfulConfig;
import com.github.thedeathlycow.thermoo.api.temperature.EnvironmentController;
import com.github.thedeathlycow.thermoo.api.temperature.EnvironmentControllerDecorator;
import com.github.thedeathlycow.thermoo.api.temperature.Soakable;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
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
        int soakChange = this.getWetChange(entity, config);
        soakChange = this.getDryChange(entity, config, soakChange);

        return soakChange;
    }

    private int getDryChange(LivingEntity entity, ScorchfulConfig config, int wetChange) {
        // dry off slowly when not being wetted
        if (wetChange <= 0 && entity.thermoo$isWet()) {
            wetChange = -config.thirstConfig.getDryRate();
        }

        // increase drying from block light
        int blockLightLevel = entity.getWorld().getLightLevel(LightType.BLOCK, entity.getBlockPos());
        if (blockLightLevel > 0) {
            wetChange -= blockLightLevel / 4;
        }

        if (entity.isOnFire()) {
            wetChange -= config.thirstConfig.getOnFireDryDate();
        }

        if (wetChange < 0 && entity.isPlayer() && entity.thermoo$isWet() && entity.getRandom().nextBoolean()) {
            ScorchfulComponents.PLAYER.get(entity).tickRehydration(config);
        }

        return wetChange;
    }

    private int getWetChange(LivingEntity entity, ScorchfulConfig config) {
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

}
