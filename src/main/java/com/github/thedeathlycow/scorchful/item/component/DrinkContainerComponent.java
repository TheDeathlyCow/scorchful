package com.github.thedeathlycow.scorchful.item.component;

import com.github.thedeathlycow.scorchful.registry.SDataComponentTypes;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

public record DrinkContainerComponent(int numDrinks, int maxDrinks) implements TooltipProvider {
    public static final Style TOOLTIP_STYLE = Style.EMPTY
            .withColor(ChatFormatting.AQUA);

    public static final DrinkContainerComponent DEFAULT = new DrinkContainerComponent(0, 16);

    private static final Codec<DrinkContainerComponent> VALUE_CODEC = RecordCodecBuilder.<DrinkContainerComponent>create(
            instance -> instance.group(
                    Codec.INT
                            .fieldOf("num_drinks")
                            .forGetter(DrinkContainerComponent::numDrinks),
                    ExtraCodecs.POSITIVE_INT
                            .optionalFieldOf("max_drinks", DEFAULT.maxDrinks)
                            .forGetter(DrinkContainerComponent::maxDrinks)
            ).apply(instance, DrinkContainerComponent::new)
    ).validate(component -> {
        if (component.numDrinks < 0 || component.numDrinks > component.maxDrinks) {
            return DataResult.error(() -> "Num drinks not in range [0, " + component.maxDrinks + "]: " + component.numDrinks);
        } else {
            return DataResult.success(component);
        }
    });

    public static final Codec<DrinkContainerComponent> CODEC = Codec.either(ExtraCodecs.intRange(0, DEFAULT.maxDrinks), VALUE_CODEC)
            .xmap(
                    either -> either.map(i -> new DrinkContainerComponent(i, DEFAULT.maxDrinks), container -> container),
                    Either::right
            );

    public static final StreamCodec<RegistryFriendlyByteBuf, DrinkContainerComponent> PACKET_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            DrinkContainerComponent::numDrinks,
            ByteBufCodecs.VAR_INT,
            DrinkContainerComponent::maxDrinks,
            DrinkContainerComponent::new
    );

    public static DrinkContainerComponent addDrinks(ItemStack stack, int value) {
        return stack.update(
                SDataComponentTypes.DRINK_CONTAINER,
                DEFAULT,
                current -> current.addDrinks(value)
        );
    }

    public static DrinkContainerComponent fillCompletely(ItemStack stack) {
        return stack.update(
                SDataComponentTypes.DRINK_CONTAINER,
                DEFAULT,
                current -> current.addDrinks(current.maxDrinks)
        );
    }

    public DrinkContainerComponent addDrinks(int value) {
        return new DrinkContainerComponent(
                Mth.clamp(this.numDrinks + value, 0, this.maxDrinks),
                this.maxDrinks
        );
    }

    public float getCurrentFill() {
        return (float) this.numDrinks / this.maxDrinks;
    }

    public boolean hasDrink() {
        return this.numDrinks > 0;
    }

    public boolean isEmpty() {
        return !this.hasDrink();
    }

    public boolean isFull() {
        return this.numDrinks >= this.maxDrinks;
    }

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> tooltip, TooltipFlag type, DataComponentGetter components) {
        MutableComponent text = this.hasDrink()
                ? Component.translatable("item.scorchful.water_skin.tooltip.count", this.numDrinks(), this.maxDrinks())
                : Component.translatable("item.scorchful.water_skin.tooltip.empty");
        text.setStyle(TOOLTIP_STYLE);
        tooltip.accept(text);
    }
}