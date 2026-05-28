package com.github.thedeathlycow.scorchful.registry;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.item.WaterSkinItem;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.Contract;

import java.util.function.Supplier;

public class SItemGroups {
    public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB, Scorchful.MODID);

    public static final Supplier<CreativeModeTab> SCORCHFUL = REGISTRY.register(
            "main",
            () -> CreativeModeTab.builder()
                    .icon(SItems.SUN_HAT::getDefaultInstance)
                    .title(Component.translatable("item_group.scorchful"))
                    .displayItems((context, entries) -> {
                        entries.accept(SItems.SUN_HAT.getDefaultInstance());
                        entries.accept(Items.TURTLE_HELMET.getDefaultInstance());
                        entries.accept(SItems.TURTLE_CHESTPLATE.getDefaultInstance());
                        entries.accept(SItems.TURTLE_LEGGINGS.getDefaultInstance());
                        entries.accept(SItems.TURTLE_BOOTS.getDefaultInstance());

                        entries.accept(SItems.WATER_SKIN.getDefaultInstance());
                        entries.accept(makeFilledWaterSkin());
                        entries.accept(SItems.CACTUS_JUICE.getDefaultInstance());

                        entries.accept(SItems.CRIMSON_LILY.getDefaultInstance());
                        entries.accept(SItems.WARPED_LILY.getDefaultInstance());
                        entries.accept(SItems.ROOTED_NETHERRACK.getDefaultInstance());
                        entries.accept(SItems.ROOTED_CRIMSON_NYLIUM.getDefaultInstance());
                        entries.accept(SItems.ROOTED_WARPED_NYLIUM.getDefaultInstance());

                        entries.accept(SItems.SAND_PILE.getDefaultInstance());
                        entries.accept(SItems.RED_SAND_PILE.getDefaultInstance());
                    }).build()
    );

    public static void initialize() {
        Scorchful.LOGGER.debug("Initialized Scorchful item groups");
    }

    @Contract("->new")
    public static ItemStack makeFilledWaterSkin() {
        var filledWaterSkin = SItems.WATER_SKIN.getDefaultInstance();
        WaterSkinItem.addDrinks(filledWaterSkin, WaterSkinItem.MAX_DRINKS);
        return filledWaterSkin;
    }

    private SItemGroups() {
    }
}
