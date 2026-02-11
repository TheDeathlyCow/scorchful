package com.github.thedeathlycow.scorchful.item;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.config.ScorchfulConfig;
import com.github.thedeathlycow.scorchful.config.section.ItemConfig;
import com.github.thedeathlycow.scorchful.registry.SEntityAttributes;
import com.github.thedeathlycow.scorchful.registry.SItems;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public class TurtleArmorEffects {
    public static void update(Player player) {
        if (player.isEyeInFluid(FluidTags.WATER)) {
            return;
        }

        ItemConfig config = ScorchfulConfig.getItemConfig();
        if (!config.isTurtleArmorEffectsEnabled()) {
            return;
        }

        double lungCapacitySeconds = player.getAttributeValue(SEntityAttributes.LUNG_CAPACITY) * config.getTurtleArmorLungCapacityMultiplier();
        int lungCapacityTicks = Mth.ceil(lungCapacitySeconds * 20);

        if (lungCapacityTicks > 0) {
            player.addEffect(
                    new MobEffectInstance(
                            MobEffects.WATER_BREATHING,
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
                    builder -> addLungCapacity(builder, EquipmentSlotGroup.HEAD)
            );
            context.modify(
                    SItems.TURTLE_CHESTPLATE,
                    builder -> addLungCapacity(builder, EquipmentSlotGroup.CHEST)
            );
            context.modify(
                    SItems.TURTLE_LEGGINGS,
                    builder -> addLungCapacity(builder, EquipmentSlotGroup.LEGS)
            );
            context.modify(
                    SItems.TURTLE_BOOTS,
                    builder -> addLungCapacity(builder, EquipmentSlotGroup.FEET)
            );
        });
    }

    private static void addLungCapacity(
            DataComponentMap.Builder builder,
            EquipmentSlotGroup slot
    ) {
        ItemAttributeModifiers attributes = builder.getOrDefault(
                DataComponents.ATTRIBUTE_MODIFIERS,
                ItemAttributeModifiers.EMPTY
        );

        attributes = attributes
                .withModifierAdded(
                        SEntityAttributes.LUNG_CAPACITY,
                        new AttributeModifier(
                                Scorchful.id("lung_capacity/").withSuffix(slot.getSerializedName()),
                                10.0,
                                AttributeModifier.Operation.ADD_VALUE
                        ),
                        slot
                );

        builder.set(DataComponents.ATTRIBUTE_MODIFIERS, attributes);
    }

    private TurtleArmorEffects() {

    }
}
