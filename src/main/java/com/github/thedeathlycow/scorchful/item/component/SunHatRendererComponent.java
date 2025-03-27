package com.github.thedeathlycow.scorchful.item.component;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.tooltip.TooltipAppender;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public record SunHatRendererComponent(
        boolean replaceArmorModel
) implements TooltipAppender {
    public static final SunHatRendererComponent DEFAULT = new SunHatRendererComponent(true);

    public static final Codec<SunHatRendererComponent> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.BOOL
                            .lenientOptionalFieldOf("replace_armor_model", DEFAULT.replaceArmorModel)
                            .forGetter(SunHatRendererComponent::replaceArmorModel)
            ).apply(instance, SunHatRendererComponent::new)
    );

    public static final PacketCodec<RegistryByteBuf, SunHatRendererComponent> PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.BOOLEAN,
            SunHatRendererComponent::replaceArmorModel,
            SunHatRendererComponent::new
    );

    public static final Identifier SHADE_OVERLAY_TEXTURE = Scorchful.id("misc/shade_overlay");

    private static final Text TOOLTIP = Text.translatable(
            "item.scorchful.sun_hat.tooltip"
    ).setStyle(Style.EMPTY.withColor(Formatting.BLUE));

    @Override
    public void appendTooltip(Item.TooltipContext context, Consumer<Text> tooltip, TooltipType type) {
        tooltip.accept(TOOLTIP);
    }
}