package com.github.thedeathlycow.scorchful.mixin.client;

import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GameRenderer.class)
public interface  GameRendererAccessor {

    @Invoker("loadPostProcessor")
    void invokeLoadPostProcessor(Identifier id);

}
