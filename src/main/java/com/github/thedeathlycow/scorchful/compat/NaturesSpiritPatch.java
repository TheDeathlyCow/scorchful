package com.github.thedeathlycow.scorchful.compat;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.block.SandCauldronBehaviours;
import com.github.thedeathlycow.scorchful.block.SandCauldronBlock;
import com.github.thedeathlycow.scorchful.block.SandPileBlock;
import com.github.thedeathlycow.scorchful.registry.SBlocks;
import com.github.thedeathlycow.scorchful.registry.SItems;
import com.github.thedeathlycow.scorchful.server.SandAccumulation;
import com.github.thedeathlycow.scorchful.server.Sandstorms;
import com.github.thedeathlycow.thermoo.impl.compat.init.DependentModInitializer;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.hibiscus.naturespirit.registration.NSMiscBlocks;
import net.minecraft.block.*;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.SoundEvents;

public class NaturesSpiritPatch implements DependentModInitializer {
    @Override
    public void onInitialize() {
        Block pinkSandPileBlock = SBlocks.register(
                "natures_spirit/pink_sand_pile",
                settings -> new SandPileBlock(
                        0xDAAF88,
                        settings
                                .mapColor(MapColor.RAW_IRON_PINK)
                                .instrument(NoteBlockInstrument.SNARE)
                                .strength(0.5f)
                                .replaceable()
                                .notSolid()
                                .blockVision((state, world, pos) -> state.get(SnowBlock.LAYERS) >= SandPileBlock.MAX_LAYERS)
                                .pistonBehavior(PistonBehavior.DESTROY)
                ),
                AbstractBlock.Settings.copy(Blocks.SAND)
        );

        Block pinkSandCauldronBlock = SBlocks.register(
                "natures_spirit/pink_sand_cauldron",
                settings -> new SandCauldronBlock(
                        Sandstorms.SandstormType.PINK,
                        getPinkSandCauldronBehavior(),
                        settings
                ),
                AbstractBlock.Settings.copy(Blocks.CAULDRON)
        );

        Item pinkSandPileItem = SItems.register(
                "natures_spirit/pink_sand_pile",
                settings -> new BlockItem(pinkSandPileBlock, settings)
        );

        SandAccumulation.SAND_PILES.put(Sandstorms.SandstormType.PINK, pinkSandPileBlock);
        SandAccumulation.SAND_CAULDRONS.put(Sandstorms.SandstormType.PINK, pinkSandCauldronBlock);

        RegistryKey<ItemGroup> group = RegistryKey.of(RegistryKeys.ITEM_GROUP, Scorchful.id("main"));
        ItemGroupEvents.modifyEntriesEvent(group).register(entries -> {
            entries.add(pinkSandPileItem.getDefaultStack());
        });

        CauldronBehavior.EMPTY_CAULDRON_BEHAVIOR.map().put(
                NSMiscBlocks.PINK_SAND.asItem(),
                SandCauldronBehaviours.fillWithSand(
                        pinkSandCauldronBlock.getDefaultState()
                                .with(SandCauldronBlock.LEVEL, SandCauldronBlock.MAX_LEVEL)
                )
        );
    }

    @Override
    public String[] getRequiredModIds() {
        return new String[]{ScorchfulIntegrations.NATURES_SPIRIT_ID};
    }

    private static CauldronBehavior.CauldronBehaviorMap getPinkSandCauldronBehavior() {
        CauldronBehavior.CauldronBehaviorMap map = CauldronBehavior.createMap("scorchful_natures_spirit_pink_sand_cauldron");

        if (map.map() instanceof Object2ObjectOpenHashMap<Item, CauldronBehavior> openHashMap) {
            openHashMap.defaultReturnValue((state, world, pos, player, hand, stack) -> {
                return SandCauldronBehaviours.emptyBlockFromCauldron(
                        state,
                        world,
                        pos,
                        player,
                        stack,
                        NSMiscBlocks.PINK_SAND.asItem().getDefaultStack(),
                        SoundEvents.BLOCK_SAND_PLACE
                );
            });
        } else {
            Scorchful.LOGGER.error("Unable to register default red sand cauldron behaviour");
        }

        return map;
    }
}