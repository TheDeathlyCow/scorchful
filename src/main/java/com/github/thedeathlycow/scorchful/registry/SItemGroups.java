package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.item.WaterSkinItem;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Contract;

public class SItemGroups {
    public static final ItemGroup SCORCHFUL = FabricItemGroup.builder()
            .icon(() -> new ItemStack(SItems.SUN_HAT))
            .displayName(Text.translatable("item_group.scorchful"))
            .entries((context, entries) -> {
                entries.add(new ItemStack(SItems.SUN_HAT));
                entries.add(new ItemStack(Items.TURTLE_HELMET));
                entries.add(new ItemStack(SItems.TURTLE_CHESTPLATE));
                entries.add(new ItemStack(SItems.TURTLE_LEGGINGS));
                entries.add(new ItemStack(SItems.TURTLE_BOOTS));

                entries.add(new ItemStack(SItems.WATER_SKIN));
                entries.add(makeFilledWaterSkin());
                entries.add(new ItemStack(SItems.CACTUS_JUICE));

                entries.add(new ItemStack(SItems.CRIMSON_LILY));
                entries.add(new ItemStack(SItems.WARPED_LILY));
                entries.add(new ItemStack(SItems.ROOTED_NETHERRACK));
                entries.add(new ItemStack(SItems.ROOTED_CRIMSON_NYLIUM));
                entries.add(new ItemStack(SItems.ROOTED_WARPED_NYLIUM));

                entries.add(new ItemStack(SItems.SAND_PILE));
                entries.add(new ItemStack(SItems.RED_SAND_PILE));
            }).build();

    public static void registerAll() {
        Registry.register(Registries.ITEM_GROUP, Scorchful.id("main"), SCORCHFUL);
    }

    @Contract("->new")
    public static ItemStack makeFilledWaterSkin() {
        var filledWaterSkin = new ItemStack(SItems.WATER_SKIN);
        ((WaterSkinItem) SItems.WATER_SKIN).addDrinks(filledWaterSkin, WaterSkinItem.MAX_DRINKS);
        return filledWaterSkin;
    }

    private SItemGroups() {
    }
}
