package com.github.thedeathlycow.scorchful.config.schema;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public final class JsonCopyHelper {
    public static void copyBooleanProperty(JsonObject src, JsonObject dest, String propertyName) {
        dest.addProperty(propertyName, src.get(propertyName).getAsBoolean());
    }

    public static void copyFloatProperty(JsonObject src, JsonObject dest, String propertyName) {
        dest.addProperty(propertyName, src.get(propertyName).getAsFloat());
    }

    public static void rename(JsonObject json, String oldName, String newName) {
        JsonElement prop = json.remove(oldName);
        json.add(newName, prop);
    }

    private JsonCopyHelper() {

    }
}