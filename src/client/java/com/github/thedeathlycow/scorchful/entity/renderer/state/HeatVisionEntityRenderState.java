package com.github.thedeathlycow.scorchful.entity.renderer.state;

import com.github.thedeathlycow.scorchful.temperature.heatvision.data.EntityState;
import com.github.thedeathlycow.scorchful.temperature.heatvision.data.HeatVisionDefinition;
import com.github.thedeathlycow.scorchful.temperature.heatvision.data.HeatVisionType;
import net.minecraft.client.render.entity.state.EntityRenderState;

public class HeatVisionEntityRenderState extends EntityRenderState {
    public HeatVisionType renderType = HeatVisionDefinition.EMPTY.value().renderType();
    public EntityState entityState = HeatVisionDefinition.EMPTY.value().entityState().orElse(null);
}