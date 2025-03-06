package com.github.thedeathlycow.scorchful.item;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.config.HeatingConfig;
import com.github.thedeathlycow.scorchful.item.component.ExtraAttributeModifierComponent;
import com.github.thedeathlycow.scorchful.registry.tag.SItemTags;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.FluidTags;

public class TurtleArmorEffects {
    public static final ExtraAttributeModifierComponent HEAT_RESISTANCE = new ExtraAttributeModifierComponent(1.0, EntityAttributeModifier.Operation.ADD_VALUE);
    public static final ExtraAttributeModifierComponent ENVIRONMENT_HEAT_RESISTANCE = new ExtraAttributeModifierComponent(0.25, EntityAttributeModifier.Operation.ADD_VALUE);

    public static void update(PlayerEntity player) {
        if (player.isSubmergedIn(FluidTags.WATER)) {
            return;
        }

        HeatingConfig config = Scorchful.getConfig().heatingConfig;
        if (!config.isTurtleArmorEffectsEnabled()) {
            return;
        }

        int durationPerPiece = config.getWaterBreathingDurationPerTurtleArmorPieceSeconds() * 20;

        int totalDuration = 0;
        for (ItemStack stack : player.getArmorItems()) {
            if (stack.isIn(SItemTags.TURTLE_ARMOR)) {
                totalDuration += durationPerPiece;
            }
        }

        if (totalDuration > 0) {
            player.addStatusEffect(
                    new StatusEffectInstance(
                            StatusEffects.WATER_BREATHING,
                            totalDuration, 0,
                            false, false, true
                    )
            );
        }
    }

    private TurtleArmorEffects() {

    }
}
