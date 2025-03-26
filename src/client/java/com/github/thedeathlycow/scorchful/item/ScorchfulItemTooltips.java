package com.github.thedeathlycow.scorchful.item;

import com.github.thedeathlycow.scorchful.registry.SDataComponentTypes;
import com.github.thedeathlycow.scorchful.registry.tag.SItemTags;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.component.ComponentType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipAppender;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.Registries;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.function.Consumer;

public final class ScorchfulItemTooltips implements ItemTooltipCallback {
    private static final Text COOLING_TOOLTIP = Text.translatable("item.scorchful.tooltip.cooling")
            .setStyle(Style.EMPTY.withColor(Formatting.AQUA));

    @Override
    public void getTooltip(ItemStack stack, Item.TooltipContext context, TooltipType tooltipType, List<Text> tooltip) {
        Consumer<Text> builder = text -> addTooltipBeforeAdvanced(stack, tooltipType, tooltip, text);

        this.appendTooltip(stack, SDataComponentTypes.DRINK_CONTAINER, context, tooltipType, builder);
        this.appendTooltip(stack, SDataComponentTypes.DRINK_LEVEL, context, tooltipType, builder);

        if (stack.isIn(SItemTags.IS_COOLING_FOOD)) {
            builder.accept(COOLING_TOOLTIP);
        }
    }

    private <T extends TooltipAppender> void appendTooltip(
            ItemStack stack,
            ComponentType<T> componentType,
            Item.TooltipContext context,
            TooltipType type,
            Consumer<Text> textConsumer
    ) {
        T tooltipAppender = stack.get(componentType);
        if (tooltipAppender != null) {
            tooltipAppender.appendTooltip(context, textConsumer, type);
        }
    }

    private static void addTooltipBeforeAdvanced(
            ItemStack stack,
            TooltipType tooltipType,
            List<Text> lines,
            Text tooltipText
    ) {
        if (!tooltipType.isAdvanced()) {
            lines.add(tooltipText);
            return;
        }

        Identifier identifier = Registries.ITEM.getId(stack.getItem());
        Text idAsText = Text.literal(identifier.toString());

        for (int i = lines.size() - 1; i >= 0; i--) {
            if (lines.get(i).contains(idAsText)) {
                lines.add(i, tooltipText);
                return;
            }
        }
    }
}
