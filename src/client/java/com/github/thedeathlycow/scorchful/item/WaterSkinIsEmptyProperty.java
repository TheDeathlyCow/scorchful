package com.github.thedeathlycow.scorchful.item;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.render.item.property.bool.BooleanProperty;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ModelTransformationMode;
import org.jetbrains.annotations.Nullable;

public record WaterSkinIsEmptyProperty() implements BooleanProperty {
    public static final MapCodec<WaterSkinIsEmptyProperty> CODEC = MapCodec.unit(new WaterSkinIsEmptyProperty());

    @Override
    public boolean getValue(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity user, int seed, ModelTransformationMode modelTransformationMode) {
        return WaterSkinItem.getNumDrinks(stack) == 0;
    }

    @Override
    public MapCodec<? extends BooleanProperty> getCodec() {
        return CODEC;
    }
}