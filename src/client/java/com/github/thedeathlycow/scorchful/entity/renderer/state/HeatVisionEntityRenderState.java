package com.github.thedeathlycow.scorchful.entity.renderer.state;

import com.github.thedeathlycow.scorchful.temperature.heatvision.v2.HeatVisionType;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.util.Identifier;

public class HeatVisionEntityRenderState extends EntityRenderState {
    public Identifier rendererID = HeatVisionType.EMPTY.value().rendererID();
}