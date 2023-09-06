package com.github.thedeathlycow.scorchful.temperature;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.config.ScorchfulConfig;
import com.github.thedeathlycow.scorchful.registry.tag.SBlockTags;
import com.github.thedeathlycow.thermoo.api.temperature.EnvironmentController;
import com.github.thedeathlycow.thermoo.api.temperature.EnvironmentControllerDecorator;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

public class AmbientTemperatureController extends EnvironmentControllerDecorator {

    /**
     * Constructs a decorator out of a base controller
     *
     * @param controller The base {@link #controller}
     */
    public AmbientTemperatureController(EnvironmentController controller) {
        super(controller);
    }

    @Override
    public int getFloorTemperature(LivingEntity entity, World world, BlockState state, BlockPos pos) {
        if (state.isIn(SBlockTags.HEAVY_ICE)) {
            ScorchfulConfig config = Scorchful.getConfig();
            return config.heatingConfig.getCoolingFromIce();
        } else {
            return controller.getFloorTemperature(entity, world, state, pos);
        }
    }

    @Override
    public int getEnvironmentTemperatureForPlayer(PlayerEntity player, int localTemperature) {
        return super.getEnvironmentTemperatureForPlayer(player, localTemperature);
    }

    @Override
    public int getHeatAtLocation(World world, BlockPos pos) {
        int skylight = world.getLightLevel(LightType.SKY, pos) - world.getAmbientDarkness();

        ScorchfulConfig config = Scorchful.getConfig();
        int minLevel = config.heatingConfig.getGetMinSkyLightLevelForHeat();

        int warmth = controller.getHeatAtLocation(world, pos);
        if (skylight >= minLevel) {
            warmth += config.heatingConfig.getHeatPerSkylightLevel() * (skylight - minLevel);
        }

        return warmth;

    }
}
