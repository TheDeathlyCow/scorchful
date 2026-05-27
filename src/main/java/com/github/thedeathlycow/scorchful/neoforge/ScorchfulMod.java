package com.github.thedeathlycow.scorchful.neoforge;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.attachment.ScorchfulEntityAttachments;
import com.github.thedeathlycow.scorchful.compat.AccessoriesIntegration;
import com.github.thedeathlycow.scorchful.compat.ScorchfulIntegrations;
import com.github.thedeathlycow.scorchful.datagen.ScorchfulDataGenerator;
import com.github.thedeathlycow.scorchful.registry.SItems;
import dev.ghen.thirst.content.purity.ContainerWithPurity;
import dev.ghen.thirst.foundation.common.event.RegisterThirstValueEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@Mod(Scorchful.MODID)
public class ScorchfulMod {
    public ScorchfulMod(IEventBus modBus) {
        ScorchfulEntityAttachments.REGISTRY.register(modBus);

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
        FabricDataGenHelper.runDatagenForMod(
                Scorchful.MODID,
                Scorchful.MODID,
                new ScorchfulDataGenerator(),
                event
        );
    }

    private static void registerDrinks(RegisterThirstValueEvent event) {
        event.addContainer(new ContainerWithPurity(SItems.WATER_SKIN, SItems.WATER_SKIN));
        event.addContainer(SItems.CACTUS_JUICE);
        event.addDrink(SItems.WATER_SKIN, 4, 5);
        event.addDrink(SItems.CACTUS_JUICE, 8, 13);
    }
}