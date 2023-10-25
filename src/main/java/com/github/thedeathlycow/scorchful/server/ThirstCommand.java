package com.github.thedeathlycow.scorchful.server;

import com.github.thedeathlycow.scorchful.components.ScorchfulComponents;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class ThirstCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {

        var thirst =
                argument("target", EntityArgumentType.player())
                        .executes(
                                context -> {
                                    return run(
                                            context.getSource(),
                                            EntityArgumentType.getPlayer(context, "target")
                                    );
                                }
                        );


        dispatcher.register(
                literal("thirst").requires(src -> src.hasPermissionLevel(2))
                        .then(
                                thirst
                        )
        );
    }

    private static int run(
            ServerCommandSource source,
            PlayerEntity target
    ) {
        return ScorchfulComponents.PLAYER.get(target).getWater();
    }

}
