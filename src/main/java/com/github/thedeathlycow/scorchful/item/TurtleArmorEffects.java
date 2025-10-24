package com.github.thedeathlycow.scorchful.item;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.config.ScorchfulConfig;
import com.github.thedeathlycow.scorchful.config.section.ItemConfig;
import com.github.thedeathlycow.scorchful.registry.SEntityAttributes;
import com.github.thedeathlycow.scorchful.registry.SItems;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.MathHelper;

public class TurtleArmorEffects {
    public static void update(PlayerEntity player) {
        if (player.isSubmergedIn(FluidTags.WATER)) {
            return;
        }

        ItemConfig config = ScorchfulConfig.getItemConfig();
        if (!config.isTurtleArmorEffectsEnabled()) {
            return;
        }

        double lungCapacitySeconds = player.getAttributeValue(SEntityAttributes.LUNG_CAPACITY) * config.getTurtleArmorLungCapacityMultiplier();
        int lungCapacityTicks = MathHelper.ceil(lungCapacitySeconds * 20);

        if (lungCapacityTicks > 0) {
            player.addStatusEffect(
                    new StatusEffectInstance(
                            StatusEffects.WATER_BREATHING,
                            lungCapacityTicks, 0,
                            false, false, true
                    )
            );
        }
    }

    public static void initialize() {
        DefaultItemComponentEvents.MODIFY.register(context -> {
            context.modify(
                    Items.TURTLE_HELMET,
                    builder -> addLungCapacity(builder, AttributeModifierSlot.HEAD)
            );
            context.modify(
                    SItems.TURTLE_CHESTPLATE,
                    builder -> addLungCapacity(builder, AttributeModifierSlot.CHEST)
            );
            context.modify(
                    SItems.TURTLE_LEGGINGS,
                    builder -> addLungCapacity(builder, AttributeModifierSlot.LEGS)
            );
            context.modify(
                    SItems.TURTLE_BOOTS,
                    builder -> addLungCapacity(builder, AttributeModifierSlot.FEET)
            );
        });
    }

    private static void addLungCapacity(
            ComponentMap.Builder builder,
            AttributeModifierSlot slot
    ) {
        AttributeModifiersComponent attributes = builder.getOrDefault(
                DataComponentTypes.ATTRIBUTE_MODIFIERS,
                AttributeModifiersComponent.DEFAULT
        );

        attributes = attributes
                .with(
                        SEntityAttributes.LUNG_CAPACITY,
                        new EntityAttributeModifier(
                                Scorchful.id("lung_capacity/").withSuffixedPath(slot.asString()),
                                10.0,
                                EntityAttributeModifier.Operation.ADD_VALUE
                        ),
                        slot
                );

        builder.add(DataComponentTypes.ATTRIBUTE_MODIFIERS, attributes);
    }

    private TurtleArmorEffects() {

    }
}
