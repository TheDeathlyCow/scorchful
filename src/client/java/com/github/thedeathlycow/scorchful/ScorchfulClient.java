package com.github.thedeathlycow.scorchful;

import com.github.thedeathlycow.scorchful.item.SModelPredicates;
import com.github.thedeathlycow.scorchful.registry.SEntityModelLayers;
import com.github.thedeathlycow.scorchful.registry.SFeatureRenderers;
import net.fabricmc.api.ClientModInitializer;

public class ScorchfulClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		SModelPredicates.onInitialize();
		SEntityModelLayers.registerAll();
		SFeatureRenderers.registerAll();
	}
}