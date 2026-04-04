package com.github.thedeathlycow.scorchful.client.item;

import com.github.thedeathlycow.scorchful.item.component.DrinkContainer;
import com.github.thedeathlycow.scorchful.registry.SDataComponentTypes;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public record WaterSkinIsEmptyProperty() implements ConditionalItemModelProperty {
    public static final MapCodec<WaterSkinIsEmptyProperty> CODEC = MapCodec.unit(new WaterSkinIsEmptyProperty());

    @Override
    public boolean get(
            ItemStack stack,
            @Nullable ClientLevel world,
            @Nullable LivingEntity entity,
            int seed,
            ItemDisplayContext displayContext
    ) {
        return stack.getOrDefault(SDataComponentTypes.DRINK_CONTAINER, DrinkContainer.DEFAULT).isEmpty();
    }

    @Override
    public MapCodec<? extends ConditionalItemModelProperty> type() {
        return CODEC;
    }
}