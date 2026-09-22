package com.github.thedeathlycow.scorchful.neoforge;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.attachment.ScorchfulEntityAttachments;
import com.github.thedeathlycow.scorchful.compat.AccessoriesIntegration;
import com.github.thedeathlycow.scorchful.compat.ScorchfulIntegrations;
import com.github.thedeathlycow.scorchful.datagen.generator.BlockTagGenerator;
import com.github.thedeathlycow.scorchful.datagen.generator.ClimateBiomeTagGenerator;
import com.github.thedeathlycow.scorchful.datagen.generator.ItemTagGenerator;
import com.github.thedeathlycow.scorchful.registry.SItemGroups;
import com.github.thedeathlycow.scorchful.registry.SItems;
import dev.ghen.thirst.content.purity.ContainerWithPurity;
import dev.ghen.thirst.foundation.common.event.RegisterThirstValueEvent;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.concurrent.CompletableFuture;

@Mod(Scorchful.MODID)
public class ScorchfulMod {
    public ScorchfulMod(IEventBus modBus) {
        ScorchfulEntityAttachments.REGISTRY.register(modBus);
        SItemGroups.REGISTRY.register(modBus);

        NeoForge.EVENT_BUS.addListener(ScorchfulMod::onTick);
        modBus.addListener(ScorchfulMod::runDatagen);

        if (ScorchfulIntegrations.isThirstWasTakenLoaded()) {
            NeoForge.EVENT_BUS.addListener(ScorchfulMod::registerDrinks);
        }

        AccessoriesIntegration.removeAccessoriesRenderer();
    }

    private static void onTick(PlayerTickEvent.Post event) {
        if (!event.getEntity().level().isClientSide()) {
            event.getEntity().getData(ScorchfulEntityAttachments.PLAYER_WATER).serverTick();
        }
    }

    private static void runDatagen(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        generator.addProvider(
                event.includeServer(),
                new ClimateBiomeTagGenerator(packOutput, lookupProvider, existingFileHelper)
        );

        BlockTagGenerator blockTags = new BlockTagGenerator(packOutput, lookupProvider, existingFileHelper);
        generator.addProvider(event.includeServer(), blockTags);
        generator.addProvider(event.includeServer(), new ItemTagGenerator(packOutput, lookupProvider, blockTags.contentsGetter(), existingFileHelper));
    }

    private static void registerDrinks(RegisterThirstValueEvent event) {
        event.addContainer(new ContainerWithPurity(SItems.WATER_SKIN, SItems.WATER_SKIN));
        event.addContainer(SItems.CACTUS_JUICE);
        event.addDrink(SItems.WATER_SKIN, 4, 5);
        event.addDrink(SItems.CACTUS_JUICE, 8, 13);
    }
}