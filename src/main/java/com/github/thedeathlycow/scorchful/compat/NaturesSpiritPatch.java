package com.github.thedeathlycow.scorchful.compat;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.block.SandCauldronBehaviours;
import com.github.thedeathlycow.scorchful.block.SandCauldronBlock;
import com.github.thedeathlycow.scorchful.block.SandPileBlock;
import com.github.thedeathlycow.scorchful.mixin.accessor.PointOfInterestTypeAccessor;
import com.github.thedeathlycow.scorchful.registry.SBlocks;
import com.github.thedeathlycow.scorchful.registry.SItems;
import com.github.thedeathlycow.scorchful.registry.SPointsOfInterest;
import com.github.thedeathlycow.scorchful.server.SandAccumulation;
import com.github.thedeathlycow.scorchful.server.Sandstorms;
import com.github.thedeathlycow.thermoo.impl.compat.init.DependentModInitializer;
import com.google.common.collect.ImmutableSet;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.hibiscus.naturespirit.registration.NSBlocks;
import net.minecraft.core.Holder;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import java.util.HashSet;
import java.util.Set;

public class NaturesSpiritPatch implements DependentModInitializer {
    @Override
    public void onInitialize() {
        Block pinkSandPileBlock = SBlocks.register(
                "natures_spirit/pink_sand_pile",
                settings -> new SandPileBlock(
                        0xDAAF88,
                        settings
                                .mapColor(MapColor.RAW_IRON)
                                .instrument(NoteBlockInstrument.SNARE)
                                .strength(0.5f)
                                .replaceable()
                                .forceSolidOff()
                                .isViewBlocking((state, world, pos) -> state.getValue(SnowLayerBlock.LAYERS) >= SandPileBlock.MAX_LAYERS)
                                .pushReaction(PushReaction.DESTROY)
                ),
                BlockBehaviour.Properties.ofFullCopy(Blocks.SAND)
        );

        Block pinkSandCauldronBlock = SBlocks.register(
                "natures_spirit/pink_sand_cauldron",
                settings -> new SandCauldronBlock(
                        Sandstorms.SandstormType.PINK,
                        getPinkSandCauldronBehavior(),
                        settings
                ),
                BlockBehaviour.Properties.ofFullCopy(Blocks.CAULDRON)
        );

        Item pinkSandPileItem = SItems.register(
                "natures_spirit/pink_sand_pile",
                settings -> new BlockItem(pinkSandPileBlock, settings)
        );

        SandAccumulation.SAND_PILES.put(Sandstorms.SandstormType.PINK, pinkSandPileBlock);
        SandAccumulation.SAND_CAULDRONS.put(Sandstorms.SandstormType.PINK, pinkSandCauldronBlock);

        ResourceKey<CreativeModeTab> group = ResourceKey.create(Registries.CREATIVE_MODE_TAB, Scorchful.id("main"));
        ItemGroupEvents.modifyEntriesEvent(group).register(entries -> {
            entries.accept(pinkSandPileItem.getDefaultInstance());
        });

        CauldronInteraction.EMPTY.map().put(
                NSBlocks.PINK_SAND.asItem(),
                SandCauldronBehaviours.fillWithSand(
                        pinkSandCauldronBlock.defaultBlockState()
                                .setValue(SandCauldronBlock.LEVEL, SandCauldronBlock.MAX_LEVEL)
                )
        );

        Holder<PoiType> leatherWorkerPOI = BuiltInRegistries.POINT_OF_INTEREST_TYPE
                .getHolder(PoiTypes.LEATHERWORKER)
                .orElseThrow();

        Set<BlockState> blockStates = new HashSet<>(pinkSandCauldronBlock.getStateDefinition().getPossibleStates());

        ((PointOfInterestTypeAccessor) (Object) leatherWorkerPOI.value()).scorchful$setBlockStates(
                ImmutableSet.<BlockState>builder()
                        .addAll(leatherWorkerPOI.value().matchingStates())
                        .addAll(blockStates)
                        .build()
        );

        SPointsOfInterest.registerStates(leatherWorkerPOI, blockStates);
    }

    @Override
    public String[] getRequiredModIds() {
        return new String[]{ScorchfulIntegrations.NATURES_SPIRIT_ID};
    }

    private static CauldronInteraction.InteractionMap getPinkSandCauldronBehavior() {
        CauldronInteraction.InteractionMap map = CauldronInteraction.newInteractionMap("scorchful_natures_spirit_pink_sand_cauldron");

        if (map.map() instanceof Object2ObjectOpenHashMap<Item, CauldronInteraction> openHashMap) {
            openHashMap.defaultReturnValue((state, world, pos, player, hand, stack) -> {
                return SandCauldronBehaviours.emptyBlockFromCauldron(
                        state,
                        world,
                        pos,
                        player,
                        stack,
                        NSBlocks.PINK_SAND.asItem().getDefaultInstance(),
                        SoundEvents.SAND_PLACE
                );
            });
        } else {
            Scorchful.LOGGER.error("Unable to register default red sand cauldron behaviour");
        }

        return map;
    }
}