package com.github.thedeathlycow.scorchful.item.component;

import com.github.thedeathlycow.scorchful.event.ModifyItemAttributeModifiersCallback;
import com.github.thedeathlycow.scorchful.mixin.accessor.AttributeModifiersComponentBuilderAccessor;
import com.google.common.collect.MapMaker;
import it.unimi.dsi.fastutil.Pair;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ModifyItemAttributeModifiersImpl {
    private static final Map<ItemStack, AttributeModifiersComponent> CACHE = new MapMaker()
            .weakKeys()
            .makeMap();

    public static void initialize() {
        ServerLifecycleEvents.SERVER_STARTING.register(server -> CACHE.clear());
        ServerLifecycleEvents.START_DATA_PACK_RELOAD.register((server, resourceManager) -> CACHE.clear());
    }

    public static AttributeModifiersComponent invoke(ItemStack stack, AttributeModifiersComponent base) {
        return CACHE.computeIfAbsent(
                stack,
                s -> {
                    AttributeModifiersComponent.Builder builder = AttributeModifiersComponent.builder();
                    AttributeModifiersComponentBuilderAccessor accessor = (AttributeModifiersComponentBuilderAccessor) builder;
                    accessor.scorchful$getEntries().addAll(base.modifiers());

                    ModifyItemAttributeModifiersCallback.EVENT.invoker().modifyAttributeModifiers(s, builder);

                    return new AttributeModifiersComponent(removeDuplicates(builder.build().modifiers()), base.showInTooltip());
                }
        );
    }

    private static List<AttributeModifiersComponent.Entry> removeDuplicates(Collection<AttributeModifiersComponent.Entry> modifiers) {
        Map<Pair<RegistryKey<EntityAttribute>, Identifier>, AttributeModifiersComponent.Entry> map = new LinkedHashMap<>();

        // de-duplicates the modifiers to remove any entries that modify the same attribute and have the same ID
        for (var modifier : modifiers) {
            Pair<RegistryKey<EntityAttribute>, Identifier> key = Pair.of(
                    modifier.attribute().getKey().orElseThrow(),
                    modifier.modifier().id()
            );
            map.put(key, modifier);
        }

        return map.values().stream().toList();
    }

    private ModifyItemAttributeModifiersImpl() {

    }
}