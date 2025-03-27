---
title: 📦 Component Types
---

# Component Types

This page documents the various component types added by Scorchful.

## Item Components

These are the new component types added for items. For the full format in Vanilla, see: [https://minecraft.wiki/w/Data_component_format](https://minecraft.wiki/w/Data_component_format)

### Drink Level
> Available 1.21.1+

Sets how much water this item should restore to players when consumed. This does not apply if a thirst mod like Dehydration is installed.

- `{}` **components**: Parent tag.
    - `E` **scorchful:heat_resistance**: One of `parching`, `refreshing`, `sustaining`, or `hydrating`. The final water replenishing values are set by [config](./config.md).

---
### Num Drinks
> Available 1.21.1-1.21.3. Replaced with [`scorchful:drink_container`](#drink-container) in 1.21.4.

Sets the number of drinks in a [Water Skin](https://modded.wiki/w/Scorchful:Water_Skin).

- `{}` **components**: Parent tag.
    - `I` **scorchful:num_drinks**: Integer in the range `[0, 16]`. Used by the [Water Skin](https://modded.wiki/w/Scorchful:Water_Skin) item to determine how many drinks it has left.

---
### Drink Container
> Available: 1.21.4+, use [`scorchful:num_drinks`](#num-drinks) in previous versions.

Sets the current and max number of drinks in a drinkable item, such as a [Water Skin](https://modded.wiki/w/Scorchful:Water_Skin). This component cannot exist on a stackable item stack.

- `{}` **components**: Parent tag.
    - `{}` **scorchful:drink_container**: A compound component.
        - `I` **num_drinks**: Integer in the range `[0, max_drinks]`. Determines the number of drinks left in this item container.
        - `I` **max_drinks**: Optional positive integer. The maximum number of drinks this container may have. Defaults to `16`.

Alternative format:

- `{}` **components**: Parent tag. 
    - `I` **scorchful:drink_container**: Integer in the range `[0, 16]`. Determines the number of drinks left in this item container.

---
### Heat Resistance
> Available: 1.21.1+

This component adds Heat Resistance and Environment Heat Resistance attribute modifiers to items. This does not modify
the underlying `minecraft:attribute_modifiers` component, it only applies modifiers when the item is equipped or
displayed in a tooltip.

The attributes are documented on the [Thermoo Wiki](https://thermoo.thedeathlycow.com/entity_attributes/).

- `{}` **components**: Parent tag.
    - `{}` **scorchful:heat_resistance**: A compound component.
        - `D` **heat_resistance**: The heat resistance value to add to the entity when this item is worn.
        - `D` **environment_heat_resistance**: The environment heat resistance value to add to the entity when this item is worn.

The final attribute modifiers are applied as an `add_value` modifier for the `thermoo:heat_resistance`[^1]
and `thermoo:environment_heat_resistance`[^1] attributes with modifier IDs
of `scorchful:base_heat_resistance/${slot_id}` and `scorchful:base_environment_heat_resistance/${slot_id}`,
respectively.

!!! tip
    These base values are not currently configurable. If you wish to have more fine-grained control over the item attribute modifier values, you will need to use another mod like [Default Components](https://modrinth.com/mod/default-components) to set the default value to `0` in this component, and then add a regular attribute modifier to the underlying `minecraft:attribute_modifiers` component using a mod like [CIA](https://modrinth.com/mod/cia) or Default Components.

??? info "Default Item Heat Resistance List"
    | Item/Armor tier                            | Heat resistance modifier            |
    |--------------------------------------------|-------------------------------------|
    | All Chainmail Armor                        | Neutral                             |
    | All Golden Armor                           | Neutral                             |
    | All Iron Armor                             | Harmful (-0.5 HR, -0.125 EHR)       |
    | All Leather Armor                          | Harmful (-0.5 HR, -0.125 EHR)       |
    | All Diamond Armor                          | Harmful (-0.5 HR, -0.125 EHR)       |
    | All Fur Armor (Frostiful)                  | Very Harmful (-1 HR, -0.25 EHR)     |
    | All Fur Padded Chainmail Armor (Frostiful) | Very Harmful (-1 HR, -0.25 EHR)     |
    | Ice Skates (Frostiful)                     | Very Harmful (-1 HR, -0.25 EHR)     |
    | Armored Ice Skates (Frostiful)             | Very Harmful (-1 HR, -0.25 EHR)     |
    | All Netherite Armor                        | Protective (+0.5 HR, +0.125 EHR)    |
    | Turtle Shell                               | Very Protective (+1 HR, +0.25 EHR)  |
    | All other [Turtle Armor](./Turtle-Armor)   | Very Protective (+1 HR, +0.25 EHR)  |

---
### Modify Camera Overlay Capacity
> Available: 1.21.4+

When present, sets the opacity of the `camera_overlay` field of an [equippable component](https://minecraft.wiki/w/Data_component_format#equippable) to be the [`sunHatShadeOpacity`](./config.md#client-config) config option.

- `{}` **components**: Parent tag.
    - `{}` **scorchful:modify_camera_overlay_opacity**: Optional empty compound.

_Example:_ `/give @s scorchful:sun_hat[scorchful:modify_camera_overlay_opacity={}]`

- Gives a [Sun Hat](https://modded.wiki/w/Scorchful:Sun_Hat) that, when equipped, will set the opacity of its shade overlay texture to be set from the config.

---
### Has Sun Hat Model
> Available: 1.21.4+

When present, renders a sun hat model instead of an item sprite on a wearer's head.

- `{}` **components**: Parent tag.
    - `{}` **scorchful:has_sun_hat_model**: Optional empty compound.

_Example:_ `/give @s minecraft:bread[equippable={slot:"head"},scorchful:has_sun_hat_model={}]`

- Gives a piece of bread that can be worn in the head slot and will render a sun hat instead of a bread item sprite.

[^1]: In 1.21.1 and below, these attribute IDs are `thermoo:generic.heat_resistance` and `thermoo:generic.environment_heat_resistance`.
