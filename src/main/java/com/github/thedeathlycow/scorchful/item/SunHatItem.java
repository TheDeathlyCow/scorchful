package com.github.thedeathlycow.scorchful.item;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.registry.SDataComponentTypes;
import com.github.thedeathlycow.thermoo.api.ThermooAttributes;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Unit;

import java.util.List;

public class SunHatItem extends Item {
    public static final Identifier SHADE_OVERLAY_TEXTURE = Scorchful.id("misc/shade_overlay");
    private static final Text TOOLTIP = Text.translatable(
            "item.scorchful.sun_hat.tooltip"
    ).setStyle(Style.EMPTY.withColor(Formatting.BLUE));

    private SunHatItem(Settings settings) {
        super(settings);
    }

    public static Item createItem(Settings settings) {
        return new SunHatItem(
                settings
                        .equipmentSlot((entity, stack) -> EquipmentSlot.HEAD)
                        .attributeModifiers(SunHatItem.attributeModifiers())
                        .maxCount(1)
                        .component(
                                DataComponentTypes.EQUIPPABLE,
                                EquippableComponent.builder(EquipmentSlot.HEAD)
                                        .damageOnHurt(false)
                                        .cameraOverlay(SHADE_OVERLAY_TEXTURE)
                                        .build()
                        )
                        .component(SDataComponentTypes.MODIFY_CAMERA_OVERLAY_OPACITY, Unit.INSTANCE)
                        .component(SDataComponentTypes.HAS_SUN_HAT_MODEL, Unit.INSTANCE)
        );
    }

    private static AttributeModifiersComponent attributeModifiers() {
        return AttributeModifiersComponent.builder()
                .add(
                        ThermooAttributes.ENVIRONMENT_HEAT_RESISTANCE,
                        new EntityAttributeModifier(
                                Scorchful.id("sun_hat_resistance"),
                                0.25,
                                EntityAttributeModifier.Operation.ADD_VALUE
                        ),
                        AttributeModifierSlot.HEAD
                )
                .build();
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(TOOLTIP);
        super.appendTooltip(stack, context, tooltip, type);
    }
}
