package com.github.thedeathlycow.scorchful.neoforge;

import com.github.thedeathlycow.scorchful.Scorchful;
import com.github.thedeathlycow.scorchful.config.ScorchfulConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = Scorchful.MODID, dist = Dist.CLIENT)
public class ScorchfulClientMod {
    public ScorchfulClientMod(ModContainer mod) {
        mod.registerExtensionPoint(IConfigScreenFactory.class, (container, parent) -> {
            return AutoConfig.getConfigScreen(ScorchfulConfig.class, parent).get();
        });
    }
}