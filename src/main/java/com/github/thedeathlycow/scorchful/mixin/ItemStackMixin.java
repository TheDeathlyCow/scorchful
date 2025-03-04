package com.github.thedeathlycow.scorchful.mixin;

import com.github.thedeathlycow.scorchful.event.ModifyItemAttributeModifiersCallback;
import com.github.thedeathlycow.scorchful.event.ScorchfulItemEvents;
import com.github.thedeathlycow.scorchful.item.component.ModifyItemAttributeModifiersImpl;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.component.ComponentChanges;
import net.minecraft.component.ComponentMapImpl;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BiConsumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Shadow @Final private ComponentMapImpl components;

    @Shadow public abstract ComponentChanges getComponentChanges();

    @Inject(
            method = "<init>(Lnet/minecraft/item/ItemConvertible;ILnet/minecraft/component/ComponentMapImpl;)V",
            at = @At("TAIL")
    )
    private void modifyDefaultStack(ItemConvertible item, int count, ComponentMapImpl components, CallbackInfo ci) {
        ItemStack original = (ItemStack) (Object) this;
        ScorchfulItemEvents.GET_DEFAULT_STACK.invoker().onCreate(original);
    }

    @WrapOperation(
            method = "applyAttributeModifier",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/component/type/AttributeModifiersComponent;applyModifiers(Lnet/minecraft/component/type/AttributeModifierSlot;Ljava/util/function/BiConsumer;)V"
            )
    )
    private void hookModifyItemAttributes(
            AttributeModifiersComponent instance,
            AttributeModifierSlot slot,
            BiConsumer<RegistryEntry<EntityAttribute>, EntityAttributeModifier> attributeConsumer,
            Operation<Void> original
    ) {
        // prevent overriding modified components from commands
        if (this.getComponentChanges().get(DataComponentTypes.ATTRIBUTE_MODIFIERS) == null) {
            instance = ModifyItemAttributeModifiersImpl.invoke((ItemStack) (Object) this, slot, instance);
        }
        original.call(instance, slot, attributeConsumer);
    }

    @WrapOperation(
            method = "applyAttributeModifiers",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/component/type/AttributeModifiersComponent;applyModifiers(Lnet/minecraft/entity/EquipmentSlot;Ljava/util/function/BiConsumer;)V"
            )
    )
    private void hookModifyItemAttributes(
            AttributeModifiersComponent instance,
            EquipmentSlot slot,
            BiConsumer<RegistryEntry<EntityAttribute>, EntityAttributeModifier> attributeConsumer,
            Operation<Void> original
    ) {
        // prevent overriding modified components from commands
        if (this.getComponentChanges().get(DataComponentTypes.ATTRIBUTE_MODIFIERS) == null) {
            instance = ModifyItemAttributeModifiersImpl.invoke((ItemStack) (Object) this, AttributeModifierSlot.forEquipmentSlot(slot), instance);
        }
        original.call(instance, slot, attributeConsumer);
    }
}
