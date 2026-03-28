package com.github.thedeathlycow.scorchful.datagen.generator.client;

import com.github.thedeathlycow.scorchful.item.WaterSkinIsEmptyProperty;
import com.github.thedeathlycow.scorchful.registry.SArmorMaterials;
import com.github.thedeathlycow.scorchful.registry.SItems;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

public class SModelGenerator extends FabricModelProvider {
    private static final Identifier CHESTPLATE_TRIM_ASSET_ID_PREFIX = ItemModelGenerators.prefixForSlotTrim("chestplate");
    private static final Identifier LEGGINGS_TRIM_ASSET_ID_PREFIX = ItemModelGenerators.prefixForSlotTrim("leggings");
    private static final Identifier BOOTS_TRIM_ASSET_ID_PREFIX = ItemModelGenerators.prefixForSlotTrim("boots");

    public SModelGenerator(FabricPackOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {
        // no blockstates are generated right now
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerator) {
        itemModelGenerator.declareCustomModelItem(SItems.CRIMSON_LILY);
        itemModelGenerator.declareCustomModelItem(SItems.WARPED_LILY);
        itemModelGenerator.declareCustomModelItem(SItems.SAND_PILE);
        itemModelGenerator.declareCustomModelItem(SItems.RED_SAND_PILE);
        itemModelGenerator.declareCustomModelItem(SItems.ROOTED_CRIMSON_NYLIUM);
        itemModelGenerator.declareCustomModelItem(SItems.ROOTED_NETHERRACK);
        itemModelGenerator.declareCustomModelItem(SItems.ROOTED_WARPED_NYLIUM);


        itemModelGenerator.declareCustomModelItem(SItems.SUN_HAT);
        itemModelGenerator.generateTrimmableItem(SItems.TURTLE_CHESTPLATE, SArmorMaterials.TURTLE.assetId(), CHESTPLATE_TRIM_ASSET_ID_PREFIX, false);
        itemModelGenerator.generateTrimmableItem(SItems.TURTLE_LEGGINGS, SArmorMaterials.TURTLE.assetId(), LEGGINGS_TRIM_ASSET_ID_PREFIX, false);
        itemModelGenerator.generateTrimmableItem(SItems.TURTLE_BOOTS, SArmorMaterials.TURTLE.assetId(), BOOTS_TRIM_ASSET_ID_PREFIX, false);
        itemModelGenerator.declareCustomModelItem(SItems.CACTUS_JUICE);

        registerWaterSkin(itemModelGenerator, SItems.WATER_SKIN);
    }

    private void registerWaterSkin(ItemModelGenerators itemModelGenerator, Item item) {
        ItemModel.Unbaked empty = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item).withSuffix("/empty"));
        ItemModel.Unbaked full = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item).withSuffix("/full"));
        itemModelGenerator.itemModelOutput.accept(
                item,
                ItemModelUtils.conditional(
                        new WaterSkinIsEmptyProperty(),
                        empty,
                        full
                )
        );
    }
}