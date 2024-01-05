package com.github.thedeathlycow.scorchful;

import com.github.thedeathlycow.scorchful.item.SModelPredicates;
import net.fabricmc.api.ClientModInitializer;

public class ScorchfulClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		SModelPredicates.onInitialize();
	}
}