package com.github.thedeathlycow.scorchful;

import com.github.thedeathlycow.scorchful.compat.ScorchfulIntegrations;
import com.github.thedeathlycow.scorchful.config.ScorchfulConfig;
import com.github.thedeathlycow.scorchful.temperature.*;
import com.github.thedeathlycow.thermoo.api.temperature.event.EnvironmentControllerInitializeEvent;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
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
		AutoConfig.register(ScorchfulConfig.class, GsonConfigSerializer::new);

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

		EnvironmentControllerInitializeEvent.EVENT.register(
				EnvironmentControllerInitializeEvent.MODIFY_PHASE,
				ScorchfulModifyTemperatureController::new
		);
	}
}