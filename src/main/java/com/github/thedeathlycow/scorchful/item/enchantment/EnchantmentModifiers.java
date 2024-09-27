package com.github.thedeathlycow.scorchful.item.enchantment;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.mixin.accessor.EnchantmentBuilderAccessor;
import com.github.thedeathlycow.thermoo.api.ThermooAttributes;
import net.fabricmc.fabric.api.item.v1.EnchantmentEvents;
import net.fabricmc.fabric.api.item.v1.EnchantmentSource;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelBasedValue;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.enchantment.effect.AttributeEnchantmentEffect;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class EnchantmentModifiers {

    public static void initialize() {
        EnchantmentEvents.MODIFY.register(EnchantmentModifiers::modifyFireProtection);
    }

    private static void modifyFireProtection(RegistryKey<Enchantment> key, Enchantment.Builder builder, EnchantmentSource source) {
        if (!source.isBuiltin() || key != Enchantments.FIRE_PROTECTION) {
            return;
        }

        float max = (float) Scorchful.getConfig().combatConfig.getVeryProtectiveArmorHeatResistance();
        float maxLevel = ((EnchantmentBuilderAccessor) builder).getDefinition().maxLevel();
        float valuePerLevel = max / maxLevel;
        builder.addEffect(
                EnchantmentEffectComponentTypes.ATTRIBUTES,
                new AttributeEnchantmentEffect(
                        Scorchful.id("enchantment.fire_protection.heat_resistance"),
                        ThermooAttributes.HEAT_RESISTANCE,
                        EnchantmentLevelBasedValue.linear(valuePerLevel),
                        EntityAttributeModifier.Operation.ADD_VALUE
                )
        );
    }

}
