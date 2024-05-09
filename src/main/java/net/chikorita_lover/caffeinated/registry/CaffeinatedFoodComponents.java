package net.chikorita_lover.caffeinated.registry;

import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.effect.StatusEffectInstance;

public class CaffeinatedFoodComponents {
    public static final FoodComponent COFFEE_BERRIES = new FoodComponent.Builder().nutrition(2).saturationModifier(0.1F).build();
    public static final FoodComponent COFFEE_BOTTLE = new FoodComponent.Builder().nutrition(2).saturationModifier(0.3F).statusEffect(new StatusEffectInstance(CaffeinatedStatusEffects.CAFFEINE, 3600, 0), 1.0F).build();
    public static final FoodComponent TIRAMISU = new FoodComponent.Builder().nutrition(5).saturationModifier(0.6F).statusEffect(new StatusEffectInstance(CaffeinatedStatusEffects.CAFFEINE, 600, 1), 1.0F).build();
}
