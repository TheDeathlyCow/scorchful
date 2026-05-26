package com.github.thedeathlycow.scorchful.server;

import com.github.thedeathlycow.scorchful.attachment.ScorchfulEntityAttachments;
import com.mojang.brigadier.CommandDispatcher;
import dev.yumi.mc.core.api.YumiMods;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.world.entity.player.Player;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class ThirstCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        if (!YumiMods.get().isDevelopmentEnvironment()) {
            return;
        }

        var thirst =
                argument("target", EntityArgument.player())
                        .executes(
                                context -> {
                                    return run(
                                            context.getSource(),
                                            EntityArgument.getPlayer(context, "target")
                                    );
                                }
                        );


        dispatcher.register(
                literal("thirst").requires(src -> src.hasPermission(2))
                        .then(
                                thirst
                        )
        );
    }

    private static int run(
            CommandSourceStack source,
            Player target
    ) {
        return target.getData(ScorchfulEntityAttachments.PLAYER_WATER).getWaterDrunk();
    }

}
