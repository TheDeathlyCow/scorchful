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
            .icon(SItems.SUN_HAT::getDefaultStack)
            .displayName(Text.translatable("item_group.scorchful"))
            .entries((context, entries) -> {
                entries.add(SItems.SUN_HAT.getDefaultStack());
                entries.add(Items.TURTLE_HELMET.getDefaultStack());
                entries.add(SItems.TURTLE_CHESTPLATE.getDefaultStack());
                entries.add(SItems.TURTLE_LEGGINGS.getDefaultStack());
                entries.add(SItems.TURTLE_BOOTS.getDefaultStack());

                entries.add(SItems.WATER_SKIN.getDefaultStack());
                entries.add(makeFilledWaterSkin());
                entries.add(SItems.CACTUS_JUICE.getDefaultStack());

                entries.add(SItems.CRIMSON_LILY.getDefaultStack());
                entries.add(SItems.WARPED_LILY.getDefaultStack());
                entries.add(SItems.ROOTED_NETHERRACK.getDefaultStack());
                entries.add(SItems.ROOTED_CRIMSON_NYLIUM.getDefaultStack());
                entries.add(SItems.ROOTED_WARPED_NYLIUM.getDefaultStack());

                entries.add(SItems.SAND_PILE.getDefaultStack());
                entries.add(SItems.RED_SAND_PILE.getDefaultStack());
            }).build();

    public static void registerAll() {
        Registry.register(Registries.ITEM_GROUP, Scorchful.id("main"), SCORCHFUL);
    }

    @Contract("->new")
    public static ItemStack makeFilledWaterSkin() {
        var filledWaterSkin = SItems.WATER_SKIN.getDefaultStack();
        WaterSkinItem.addDrinks(filledWaterSkin, WaterSkinItem.MAX_DRINKS);
        return filledWaterSkin;
    }

    private SItemGroups() {
    }
}
