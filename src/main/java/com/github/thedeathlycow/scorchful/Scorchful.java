package com.github.thedeathlycow.scorchful;

import com.github.thedeathlycow.scorchful.temperature.LivingEntityThermooEventListeners;
import com.github.thedeathlycow.scorchful.temperature.PlayerThermooEventListeners;
import com.github.thedeathlycow.thermoo.api.temperature.event.LivingEntityEnvironmentEvents;
import com.github.thedeathlycow.thermoo.api.temperature.event.LivingEntityTemperatureEvents;
import com.github.thedeathlycow.thermoo.api.temperature.event.PlayerEnvironmentEvents;
import net.fabricmc.api.ModInitializer;

import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Contract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Scorchful implements ModInitializer {

	public static final String MODID = "scorchful";

    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	@Contract("_->new")
	public static Identifier id(String path) {
		return new Identifier(MODID, path);
	}

	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");

		registerThermooEventListeners();
	}

	private void registerThermooEventListeners() {

		var playerEvents = new PlayerThermooEventListeners();
		var entityEvents = new LivingEntityThermooEventListeners();

		PlayerEnvironmentEvents.TICK_BIOME_TEMPERATURE_CHANGE.register(playerEvents::applyPassiveHeating);

		LivingEntityEnvironmentEvents.TICK_HEAT_EFFECTS.register(entityEvents::tickHeatEffects);
		LivingEntityEnvironmentEvents.TICK_IN_WET_LOCATION.register(entityEvents::tickWetChange);
	}
}