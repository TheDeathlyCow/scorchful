package com.github.thedeathlycow.scorchful;

import com.github.thedeathlycow.scorchful.temperature.LivingEntityThermooEventListeners;
import com.github.thedeathlycow.scorchful.temperature.PlayerThermooEventListeners;
import com.github.thedeathlycow.thermoo.api.temperature.event.PlayerEnvironmentEvents;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Scorchful implements ModInitializer {

	public static final String MODID = "scorchful";

    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");

	}

	private void registerThermooEventListeners() {

		var playerEvents = new PlayerThermooEventListeners();
		var entityEvents = new LivingEntityThermooEventListeners();

		PlayerEnvironmentEvents.TICK_BIOME_TEMPERATURE_CHANGE.register(playerEvents::applyPassiveHeating);

	}
}