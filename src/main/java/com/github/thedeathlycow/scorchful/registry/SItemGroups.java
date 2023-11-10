package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;

public class SItemGroups {
    public static final ItemGroup SCORCHFUL = FabricItemGroup.builder()
            .icon(() -> new ItemStack(SItems.WATER_SKIN))
            .displayName(Text.translatable("itemGroup.scorchful"))
            .entries((context, entries) -> {
                entries.add(new ItemStack(SItems.WATER_SKIN));
                entries.add(new ItemStack(SItems.STRAW_HAT));
            }).build();

    public static void registerAll() {
        Registry.register(Registries.ITEM_GROUP, Scorchful.id("main"), SCORCHFUL);
    }

    private SItemGroups() {
    }
}
