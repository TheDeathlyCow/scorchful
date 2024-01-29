package com.github.thedeathlycow.scorchful.temperature;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.config.ScorchfulConfig;
import com.github.thedeathlycow.scorchful.mixin.thirst.EntityInvoker;
import com.github.thedeathlycow.thermoo.api.temperature.EnvironmentController;
import com.github.thedeathlycow.thermoo.api.temperature.EnvironmentControllerDecorator;
import com.github.thedeathlycow.thermoo.api.temperature.Soakable;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.LightType;

public class WetTickController extends EnvironmentControllerDecorator {

    public WetTickController(EnvironmentController controller) {
        super(new DryingController(new WettingController(controller)));
    }

    private static class DryingController extends EnvironmentControllerDecorator {

        private DryingController(EnvironmentController controller) {
            super(controller);
        }

        @Override
        public int getSoakChange(Soakable soakable) {
            int soakChange = controller.getSoakChange(soakable);

            if (!(soakable instanceof LivingEntity entity)) {
                return soakChange;
            }
            ScorchfulConfig config = Scorchful.getConfig();

            // dry off slowly when not being wetted
            if (soakChange <= 0 && entity.thermoo$isWet()) {
                soakChange = -config.thirstConfig.getDryRate();
            }

            // increase drying from block light
            int blockLightLevel = entity.getWorld().getLightLevel(LightType.BLOCK, entity.getBlockPos());
            if (blockLightLevel > 0) {
                soakChange -= blockLightLevel / 4;
            }

            if (entity.isOnFire()) {
                soakChange -= config.thirstConfig.getOnFireDryDate();
            }

            return soakChange;
        }
    }

    private static class WettingController extends EnvironmentControllerDecorator {

        private WettingController(EnvironmentController controller) {
            super(controller);
        }

        @Override
        public int getSoakChange(Soakable soakable) {

            int soakChange = 0;

            if (!(soakable instanceof LivingEntity entity)) {
                return soakChange;
            }

            ScorchfulConfig config = Scorchful.getConfig();
            EntityInvoker invoker = (EntityInvoker) entity;


            // immediately soak players in water
            if (entity.isSubmergedInWater() || invoker.scorchful_invokeIsInsideBubbleColumn()) {
                return entity.thermoo$getMaxWetTicks();
            }

            // add wetness from rain
            if (invoker.scorchful_invokeIsBeingRainedOn()) {
                soakChange += config.thirstConfig.getRainWetnessIncrease();
            }

            // add wetness when touching, but not submerged in, water
            if (entity.isTouchingWater() || entity.getBlockStateAtPos().isOf(Blocks.WATER_CAULDRON)) {
                soakChange += config.thirstConfig.getTouchingWaterWetnessIncrease();
            }

            return soakChange;
        }

    }

}
