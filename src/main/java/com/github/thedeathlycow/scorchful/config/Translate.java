package com.github.thedeathlycow.scorchful.config;

import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import net.minecraft.util.Util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public final class Translate {
    public static String prefixKey(ConfigClassHandler<?> handler) {
        return Util.createTranslationKey("yacl3.config", handler.id());
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Name {
        String value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface NoComment {

    }
}