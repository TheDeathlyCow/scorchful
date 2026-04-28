package com.github.thedeathlycow.scorchful.item;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.config.HeatingConfig;
import com.github.thedeathlycow.scorchful.registry.tag.SItemTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class TurtleArmorEffects {
    public static void update(Player player) {
        if (player.isEyeInFluid(FluidTags.WATER)) {
            return;
        }

        HeatingConfig config = Scorchful.getConfig().heatingConfig;
        if (!config.isTurtleArmorEffectsEnabled()) {
            return;
        }

        int durationPerPiece = config.getWaterBreathingDurationPerTurtleArmorPieceSeconds() * 20;

        int totalDuration = 0;
        for (ItemStack stack : player.getArmorSlots()) {
            if (stack.is(SItemTags.TURTLE_ARMOR)) {
                totalDuration += durationPerPiece;
            }
        }

        if (totalDuration > 0) {
            player.addEffect(
                    new MobEffectInstance(
                            MobEffects.WATER_BREATHING,
                            totalDuration, 0,
                            false, false, true
                    )
            );
        }
    }

    private TurtleArmorEffects() {

    }
}
