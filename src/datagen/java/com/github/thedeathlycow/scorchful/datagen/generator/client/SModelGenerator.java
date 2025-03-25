package com.github.thedeathlycow.scorchful.datagen.generator.client;

import com.github.thedeathlycow.scorchful.item.WaterSkinIsEmptyProperty;
import com.github.thedeathlycow.scorchful.registry.SItems;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.ItemModelGenerator;
import net.minecraft.client.data.ItemModels;
import net.minecraft.client.data.ModelIds;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.item.Item;

public class SModelGenerator extends FabricModelProvider {
    public SModelGenerator(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        // no blockstates are generated right now
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(SItems.CRIMSON_LILY);
        itemModelGenerator.register(SItems.WARPED_LILY);
        itemModelGenerator.register(SItems.SAND_PILE);
        itemModelGenerator.register(SItems.RED_SAND_PILE);

        itemModelGenerator.register(SItems.SUN_HAT);
        itemModelGenerator.register(SItems.TURTLE_CHESTPLATE);
        itemModelGenerator.register(SItems.TURTLE_LEGGINGS);
        itemModelGenerator.register(SItems.TURTLE_BOOTS);
        itemModelGenerator.register(SItems.CACTUS_JUICE);

        registerWaterSkin(itemModelGenerator, SItems.WATER_SKIN);
    }

    private void registerWaterSkin(ItemModelGenerator itemModelGenerator, Item item) {
        ItemModel.Unbaked empty = ItemModels.basic(ModelIds.getItemModelId(item).withSuffixedPath("/empty"));
        ItemModel.Unbaked full = ItemModels.basic(ModelIds.getItemModelId(item).withSuffixedPath("/full"));
        itemModelGenerator.output.accept(
                item,
                ItemModels.condition(
                        new WaterSkinIsEmptyProperty(),
                        empty,
                        full
                )
        );
    }
}