package com.github.thedeathlycow.scorchful;

import com.github.thedeathlycow.scorchful.compat.ScorchfulIntegrations;
import com.github.thedeathlycow.scorchful.config.ScorchfulConfig;
import com.github.thedeathlycow.scorchful.registry.SItems;
import com.github.thedeathlycow.scorchful.server.ThirstCommand;
import com.github.thedeathlycow.scorchful.temperature.*;
import com.github.thedeathlycow.thermoo.api.temperature.event.EnvironmentControllerInitializeEvent;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
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
		AutoConfig.register(ScorchfulConfig.class, GsonConfigSerializer::new);
		SItems.registerItems();
		CommandRegistrationCallback.EVENT.register(
				(dispatcher, registryAccess, environment) -> {
					ThirstCommand.register(dispatcher);
				});
		this.registerThermooEventListeners();
		LOGGER.info("Scorchful initialized!");
	}

	public static ScorchfulConfig getConfig() {
		return AutoConfig.getConfigHolder(ScorchfulConfig.class).getConfig();
	}


	private void registerThermooEventListeners() {
		EnvironmentControllerInitializeEvent.EVENT.register(AttributeController::new);
		EnvironmentControllerInitializeEvent.EVENT.register(
				EnvironmentControllerInitializeEvent.MODIFY_PHASE,
				PlayerAttributeController::new
		);

		EnvironmentControllerInitializeEvent.EVENT.register(AmbientTemperatureController::new);
		EnvironmentControllerInitializeEvent.EVENT.register(WetTickController::new);
	}
}