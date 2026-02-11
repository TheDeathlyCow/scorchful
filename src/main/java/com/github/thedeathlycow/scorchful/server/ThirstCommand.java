package com.github.thedeathlycow.scorchful.server;

import com.github.thedeathlycow.scorchful.components.ScorchfulComponents;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.world.entity.player.Player;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class ThirstCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {

        if (!FabricLoader.getInstance().isDevelopmentEnvironment()) {
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
                literal("thirst").requires(src -> src.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER))
                        .then(
                                thirst
                        )
        );
    }

    private static int run(
            CommandSourceStack source,
            Player target
    ) {
        return ScorchfulComponents.PLAYER_WATER.get(target).getWaterDrunk();
    }

}
