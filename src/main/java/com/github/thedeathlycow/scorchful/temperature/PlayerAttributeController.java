package com.github.thedeathlycow.scorchful.temperature;

import com.github.thedeathlycow.thermoo.api.ThermooAttributes;
import com.github.thedeathlycow.thermoo.api.temperature.EnvironmentController;
import com.github.thedeathlycow.thermoo.api.temperature.EnvironmentControllerDecorator;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;

public class PlayerAttributeController extends EnvironmentControllerDecorator {
    /**
     * Constructs a decorator out of a base controller
     *
     * @param controller The base {@link #controller}
     */
    public PlayerAttributeController(EnvironmentController controller) {
        super(controller);
    }

    @Override
    public double getBaseValueForAttribute(EntityAttribute attribute, LivingEntity entity) {
        double base = super.getBaseValueForAttribute(attribute, entity);

        if (ThermooAttributes.MAX_TEMPERATURE == attribute && entity.isPlayer()) {
            base += 5;
        }

        return base;
    }
}
