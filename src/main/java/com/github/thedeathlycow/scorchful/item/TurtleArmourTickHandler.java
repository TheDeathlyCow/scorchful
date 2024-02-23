package com.github.thedeathlycow.scorchful.item;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.registry.SItems;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.FluidTags;

public class TurtleArmourTickHandler {

    public static void tick(PlayerEntity player) {

        if (player.isSubmergedIn(FluidTags.WATER)) {
            return;
        }

        if (!Scorchful.getConfig().heatingConfig.isTurtleArmorEffectsEnabled()) {
            return;
        }

        tickChestplate(player);
        tickLegsAndBoots(player);
    }

    private static void tickChestplate(PlayerEntity player) {
        ItemStack stack = player.getEquippedStack(EquipmentSlot.CHEST);
        if (stack.isOf(SItems.TURTLE_CHESTPLATE)) {
            player.addStatusEffect(
                    new StatusEffectInstance(
                            StatusEffects.RESISTANCE,
                            200, 0,
                            false, false, true
                    )
            );
            player.addStatusEffect(
                    new StatusEffectInstance(
                            StatusEffects.SLOWNESS,
                            200, 0,
                            false, false, true
                    )
            );
        }
    }

    private static void tickLegsAndBoots(PlayerEntity player) {
        ItemStack leggings = player.getEquippedStack(EquipmentSlot.LEGS);
        ItemStack boots = player.getEquippedStack(EquipmentSlot.FEET);
        if (leggings.isOf(SItems.TURTLE_LEGGINGS) && boots.isOf(SItems.TURTLE_BOOTS)) {
            player.addStatusEffect(
                    new StatusEffectInstance(
                            StatusEffects.DOLPHINS_GRACE,
                            200, 0,
                            false, false, true
                    )
            );
        }
    }

    private TurtleArmourTickHandler() {

    }
}
