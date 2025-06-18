package com.github.thedeathlycow.scorchful.datagen.generator.client;

import com.github.thedeathlycow.scorchful.item.WaterSkinIsEmptyProperty;
import com.github.thedeathlycow.scorchful.registry.SArmorMaterials;
import com.github.thedeathlycow.scorchful.registry.SItems;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.ItemModelGenerator;
import net.minecraft.client.data.ItemModels;
import net.minecraft.client.data.ModelIds;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

public class SModelGenerator extends FabricModelProvider {
    private static final Identifier CHESTPLATE_TRIM_ASSET_ID_PREFIX = ItemModelGenerator.getTrimAssetIdPrefix("chestplate");
    private static final Identifier LEGGINGS_TRIM_ASSET_ID_PREFIX = ItemModelGenerator.getTrimAssetIdPrefix("leggings");
    private static final Identifier BOOTS_TRIM_ASSET_ID_PREFIX = ItemModelGenerator.getTrimAssetIdPrefix("boots");

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
        itemModelGenerator.register(SItems.ROOTED_CRIMSON_NYLIUM);
        itemModelGenerator.register(SItems.ROOTED_NETHERRACK);
        itemModelGenerator.register(SItems.ROOTED_WARPED_NYLIUM);


        itemModelGenerator.register(SItems.SUN_HAT);
        itemModelGenerator.registerArmor(SItems.TURTLE_CHESTPLATE, SArmorMaterials.TURTLE.assetId(), CHESTPLATE_TRIM_ASSET_ID_PREFIX, false);
        itemModelGenerator.registerArmor(SItems.TURTLE_LEGGINGS, SArmorMaterials.TURTLE.assetId(), LEGGINGS_TRIM_ASSET_ID_PREFIX, false);
        itemModelGenerator.registerArmor(SItems.TURTLE_BOOTS, SArmorMaterials.TURTLE.assetId(), BOOTS_TRIM_ASSET_ID_PREFIX, false);
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