package com.github.thedeathlycow.scorchful.neoforge;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.attachment.ScorchfulEntityAttachments;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@Mod(Scorchful.MODID)
public class ScorchfulMod {
    public ScorchfulMod(IEventBus modBus) {
        ScorchfulEntityAttachments.REGISTRY.register(modBus);

        NeoForge.EVENT_BUS.addListener(ScorchfulMod::onTick);
    }

    private static void onTick(PlayerTickEvent.Post event) {
        if (!event.getEntity().level().isClientSide()) {
            event.getEntity().getData(ScorchfulEntityAttachments.PLAYER_WATER).serverTick();
        }
    }
}