package com.github.thedeathlycow.scorchful;

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
}