package com.github.thedeathlycow.scorchful.item.component;

import com.github.thedeathlycow.scorchful.registry.SDataComponentTypes;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipAppender;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.MathHelper;

import java.util.function.Consumer;

public record DrinkContainerComponent(int numDrinks, int maxDrinks) implements TooltipAppender {
    public static final Style TOOLTIP_STYLE = Style.EMPTY
            .withColor(Formatting.AQUA);

    public static final DrinkContainerComponent DEFAULT = new DrinkContainerComponent(0, 16);

    private static final Codec<DrinkContainerComponent> VALUE_CODEC = RecordCodecBuilder.<DrinkContainerComponent>create(
            instance -> instance.group(
                    Codec.INT
                            .fieldOf("num_drinks")
                            .forGetter(DrinkContainerComponent::numDrinks),
                    Codecs.POSITIVE_INT
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

    public static final Codec<DrinkContainerComponent> CODEC = Codec.either(Codecs.rangedInt(0, DEFAULT.maxDrinks), VALUE_CODEC)
            .xmap(
                    either -> either.map(i -> new DrinkContainerComponent(i, DEFAULT.maxDrinks), container -> container),
                    Either::right
            );

    public static final PacketCodec<RegistryByteBuf, DrinkContainerComponent> PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.VAR_INT,
            DrinkContainerComponent::numDrinks,
            PacketCodecs.VAR_INT,
            DrinkContainerComponent::maxDrinks,
            DrinkContainerComponent::new
    );

    public static DrinkContainerComponent addDrinks(ItemStack stack, int value) {
        return stack.apply(
                SDataComponentTypes.DRINK_CONTAINER,
                DEFAULT,
                current -> current.addDrinks(value)
        );
    }

    public static DrinkContainerComponent fillCompletely(ItemStack stack) {
        return stack.apply(
                SDataComponentTypes.DRINK_CONTAINER,
                DEFAULT,
                current -> current.addDrinks(current.maxDrinks)
        );
    }

    public DrinkContainerComponent addDrinks(int value) {
        return new DrinkContainerComponent(
                MathHelper.clamp(this.numDrinks + value, 0, this.maxDrinks),
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
    public void appendTooltip(Item.TooltipContext context, Consumer<Text> tooltip, TooltipType type) {
        MutableText text = this.hasDrink()
                ? Text.translatable("item.scorchful.water_skin.tooltip.count", this.numDrinks(), this.maxDrinks())
                : Text.translatable("item.scorchful.water_skin.tooltip.empty");
        text.setStyle(TOOLTIP_STYLE);
        tooltip.accept(text);
    }
}