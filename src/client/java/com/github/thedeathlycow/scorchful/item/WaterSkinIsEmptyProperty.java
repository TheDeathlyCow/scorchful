package com.github.thedeathlycow.scorchful.item;

import com.github.thedeathlycow.scorchful.item.component.DrinkContainerComponent;
import com.github.thedeathlycow.scorchful.registry.SDataComponentTypes;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.render.item.property.bool.BooleanProperty;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public record WaterSkinIsEmptyProperty() implements BooleanProperty {
    public static final MapCodec<WaterSkinIsEmptyProperty> CODEC = MapCodec.unit(new WaterSkinIsEmptyProperty());

    @Override
    public boolean test(
            ItemStack stack,
            @Nullable ClientWorld world,
            @Nullable LivingEntity entity,
            int seed,
            ItemDisplayContext displayContext
    ) {
        return stack.getOrDefault(SDataComponentTypes.DRINK_CONTAINER, DrinkContainerComponent.DEFAULT).isEmpty();
    }

    @Override
    public MapCodec<? extends BooleanProperty> getCodec() {
        return CODEC;
    }
}