package com.github.thedeathlycow.scorchful.client.item;

import com.github.thedeathlycow.scorchful.api.ServerThirstPlugin;
import com.github.thedeathlycow.scorchful.compat.ScorchfulIntegrations;
import com.github.thedeathlycow.scorchful.item.component.DrinkLevelComponent;
import com.github.thedeathlycow.scorchful.registry.SDataComponentTypes;
import com.github.thedeathlycow.scorchful.registry.tag.SItemTags;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class ItemTooltips {

    private static final Component COOLING_TOOLTIP = Component.translatable("item.scorchful.tooltip.cooling")
            .setStyle(Style.EMPTY.withColor(ChatFormatting.AQUA));

    public static void appendDrinkTooltip(ItemStack stack, Item.TooltipContext context, TooltipFlag tooltipType, List<Component> tooltip) {
        if (ServerThirstPlugin.isCustomPluginLoaded()) {
            return;
        }

        DrinkLevelComponent level = stack.get(SDataComponentTypes.DRINK_LEVEL);
        if (level != null) {
            if (tooltipType.isAdvanced()) {
                addTooltipBeforeAdvanced(stack, tooltip, level.getTooltipText());
            } else {
                tooltip.add(level.getTooltipText());
            }
        }
    }

    public static void appendCoolingTooltip(ItemStack stack, Item.TooltipContext context, TooltipFlag tooltipType, List<Component> tooltip) {
        if (stack.is(SItemTags.IS_COOLING_FOOD)) {
            if (tooltipType.isAdvanced()) {
                addTooltipBeforeAdvanced(stack, tooltip, COOLING_TOOLTIP);
            } else {
                tooltip.add(COOLING_TOOLTIP);
            }
        }
    }

    private static void addTooltipBeforeAdvanced(ItemStack stack, List<Component> tooltip, Component tooltipText) {
        ResourceLocation identifier = BuiltInRegistries.ITEM.getKey(stack.getItem());
        Component idAsText = Component.literal(identifier.toString());

        for (int i = tooltip.size() - 1; i >= 0; i--) {
            if (tooltip.get(i).contains(idAsText)) {
                tooltip.add(i, tooltipText);
                return;
            }
        }
    }

    private ItemTooltips() {

    }

}
