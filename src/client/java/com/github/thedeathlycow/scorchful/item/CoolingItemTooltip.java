package com.github.thedeathlycow.scorchful.item;

import com.github.thedeathlycow.scorchful.registry.tag.SItemTags;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;
import java.util.function.Consumer;

public final class CoolingItemTooltip implements ItemTooltipCallback {
    private static final Component COOLING_TOOLTIP = Component.translatable("item.scorchful.tooltip.cooling")
            .setStyle(Style.EMPTY.withColor(ChatFormatting.AQUA));

    @Override
    public void getTooltip(ItemStack stack, Item.TooltipContext context, TooltipFlag tooltipType, List<Component> tooltip) {
        Consumer<Component> builder = text -> addTooltipBeforeAdvanced(stack, tooltipType, tooltip, text);

        if (stack.is(SItemTags.IS_COOLING_FOOD)) {
            builder.accept(COOLING_TOOLTIP);
        }
    }

    private static void addTooltipBeforeAdvanced(
            ItemStack stack,
            TooltipFlag tooltipType,
            List<Component> lines,
            Component tooltipText
    ) {
        if (!tooltipType.isAdvanced()) {
            lines.add(tooltipText);
            return;
        }

        Identifier identifier = BuiltInRegistries.ITEM.getKey(stack.getItem());
        Component idAsText = Component.literal(identifier.toString());

        for (int i = lines.size() - 1; i >= 0; i--) {
            if (lines.get(i).contains(idAsText)) {
                lines.add(i, tooltipText);
                return;
            }
        }
    }
}
