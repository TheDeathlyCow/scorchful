package com.github.thedeathlycow.scorchful;

import com.github.thedeathlycow.scorchful.config.*;
import com.github.thedeathlycow.scorchful.config.section.*;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.isxander.yacl3.api.ButtonOption;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class ScorchfulModMenu implements ModMenuApi {
    public static final String TITLE = "scorchful.title";

    public static final String CLIENT_CATEGORY = Translate.mainCategoryKey(ClientConfig.HANDLER);
    public static final String COMBAT_CATEGORY = Translate.mainCategoryKey(CombatConfig.HANDLER);
    public static final String HEATING_CATEGORY = Translate.mainCategoryKey(HeatingConfig.HANDLER);
    public static final String THIRST_CATEGORY = Translate.mainCategoryKey(ThirstConfig.HANDLER);
    public static final String WEATHER_CATEGORY = Translate.mainCategoryKey(WeatherConfig.HANDLER);
    public static final String DEHYDRATION_CATEGORY = Translate.mainCategoryKey(DehydrationConfig.HANDLER);

    public static final String CLIENT_DESC = Translate.descKey(ClientConfig.HANDLER);
    public static final String COMBAT_DESC = Translate.descKey(CombatConfig.HANDLER);
    public static final String HEATING_DESC = Translate.descKey(HeatingConfig.HANDLER);
    public static final String THIRST_DESC = Translate.descKey(ThirstConfig.HANDLER);
    public static final String WEATHER_DESC = Translate.descKey(WeatherConfig.HANDLER);
    public static final String DEHYDRATION_DESC = Translate.descKey(DehydrationConfig.HANDLER);


    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> YetAnotherConfigLib.createBuilder()
                .title(Text.literal("Immersive Storms Test"))
                .category(
                        ConfigCategory.createBuilder()
                                .name(Text.translatable(TITLE))
                                .option(createSubsectionButton(ClientConfig.HANDLER, CLIENT_CATEGORY, CLIENT_DESC))
                                .option(createSubsectionButton(CombatConfig.HANDLER, COMBAT_CATEGORY, COMBAT_DESC))
                                .option(createSubsectionButton(HeatingConfig.HANDLER, HEATING_CATEGORY, HEATING_DESC))
                                .option(createSubsectionButton(ThirstConfig.HANDLER, THIRST_CATEGORY, THIRST_DESC))
                                .option(createSubsectionButton(WeatherConfig.HANDLER, WEATHER_CATEGORY, WEATHER_DESC))
                                .option(createSubsectionButton(DehydrationConfig.HANDLER, DEHYDRATION_CATEGORY, DEHYDRATION_DESC))
                                .build()
                )
                .build()
                .generateScreen(parent);
    }

    private static ButtonOption createSubsectionButton(ConfigClassHandler<?> handler, String titleKey, String descKey) {
        return ButtonOption.createBuilder()
                .name(Text.translatable(titleKey))
                .description(
                        OptionDescription.createBuilder()
                                .text(Text.translatable(descKey))
                                .build()
                )
                .text(Text.literal(""))
                .action((yaclScreen, buttonOption) -> {
                    MinecraftClient.getInstance()
                            .setScreen(handler
                                    .generateGui()
                                    .generateScreen(yaclScreen));
                }).build();
    }
}
