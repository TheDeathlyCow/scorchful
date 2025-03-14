---
title: 🥵 Custom Temperature Effects
---
Scorchful adds a few new temperature effect types. These are further variants of the temperature effect types provided by Thermoo. Consider this page an extension of the Thermoo page for temperature effects, which you can find the full version of here: https://github.com/TheDeathlyCow/thermoo/wiki/Temperature-Effects-(Mods-and-Datapacks)

# Scorchful Temperature Effect Types

---

## Sound

Plays a sound event on an interval.

### Config JSON format

If the `type` is `scorchful:sound`, the `config` has the following format:

* `{}` **config**: Parent tag.
    * `"` **sound**: Identifier, the ID of the sound event to be played (e.g., `minecraft:entity.wolf.pant`)
    * `E` **category**: Enum. The category to play the sound in. For example: `master`, `ambient`, `voice`. See the Minecraft Wiki for full list: [https://minecraft.wiki/w/Sound#Categories](https://minecraft.wiki/w/Sound#Categories)
    * `F` `{}` **volume**: Float provider, the range of the sound.
    * `F` `{}` **pitch**: Float provider, the pitch to play the sound at.
    * `I` **interval**: Integer, how many ticks between playing the sound.
    * `T/F` **only_play_to_source**: Boolean, if true, the sound is only played to the player it this effect is active for. OPTIONAL: Defaults to `false`, playing for all nearby entities.

!!! tip
    You can disable the sound effect with the config option `enableSoundTemperatureEffects` in the [Client Config](./config.md#client-config).

---

## Change Temperature

Changes the entity's temperature on an interval.

### Config JSON format

If the `type` is `scorchful:change_temperature`, the `config` has the following format:

* `{}` **config**: Parent tag.
    * `I` **temperature_change**: Integer, how much to change the entity's temperature by.
    * `I` **interval**: Positive integer, how many ticks between the changing of the entity's temperature.
    * `E` **heating_mode**: Enum, what [mode](https://thermoo.thedeathlycow.com/api_overview/#temperature-changes-and-resistances) to apply the temperature change in. Must be one of `absolute`, `active`, or `passive`. Optional, defaults to `absolute`.

---
