package com.github.thedeathlycow.scorchful;

import com.github.thedeathlycow.scorchful.config.ClientConfig;
import com.github.thedeathlycow.scorchful.config.CombatConfig;
import com.github.thedeathlycow.scorchful.config.Translate;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.isxander.yacl3.api.ButtonOption;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class ScorchfulModMenu implements ModMenuApi {
    private static final String CLIENT_PREFIX = Translate.prefixKey(ClientConfig.HANDLER);
    private static final String COMBAT_PREFIX = Translate.prefixKey(CombatConfig.HANDLER);


    public static final String TITLE = "scorchful.title";

    public static final String CLIENT_CATEGORY = CLIENT_PREFIX + ".category.client";
    public static final String COMBAT_CATEGORY = COMBAT_PREFIX + ".category.combat";

    public static final String CLIENT_CATEGORY_DESC = CLIENT_PREFIX + ".category.desc";
    public static final String COMBAT_CATEGORY_DESC = COMBAT_PREFIX + ".category.desc";


    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> YetAnotherConfigLib.createBuilder()
                .title(Text.literal("Immersive Storms Test"))
                .category(
                        ConfigCategory.createBuilder()
                                .name(Text.translatable(TITLE))
                                .option(ButtonOption.createBuilder()
                                        .name(Text.translatable(CLIENT_CATEGORY))
                                        .description(
                                                OptionDescription.createBuilder()
                                                        .text(Text.translatable(CLIENT_CATEGORY_DESC))
                                                        .build()
                                        )
                                        .text(Text.literal(""))
                                        .action((yaclScreen, buttonOption) -> {
                                            MinecraftClient.getInstance()
                                                    .setScreen(ClientConfig.HANDLER
                                                            .generateGui()
                                                            .generateScreen(yaclScreen));
                                        }).build())
                                .option(ButtonOption.createBuilder()
                                        .name(Text.translatable(COMBAT_CATEGORY))
                                        .description(
                                                OptionDescription.createBuilder()
                                                        .text(Text.translatable(COMBAT_CATEGORY_DESC))
                                                        .build()
                                        )
                                        .text(Text.literal(""))
                                        .action((yaclScreen, buttonOption) -> {
                                            MinecraftClient.getInstance()
                                                    .setScreen(CombatConfig.HANDLER
                                                            .generateGui()
                                                            .generateScreen(yaclScreen));
                                        }).build())
                                .build()
                )
                .build()
                .generateScreen(parent);
    }

}
