package com.github.thedeathlycow.scorchful.entity.renderer.state;

import com.github.thedeathlycow.scorchful.temperature.heatvision.v2.HeatVisionDefinition;
import com.github.thedeathlycow.scorchful.temperature.heatvision.v2.HeatVisionType;
import net.minecraft.client.render.entity.state.EntityRenderState;

public class HeatVisionEntityRenderState extends EntityRenderState {
    public HeatVisionType renderType = HeatVisionDefinition.EMPTY.value().type();
}