package com.chikoritalover.caffeinated.registry;

import com.chikoritalover.caffeinated.Caffeinated;
import com.chikoritalover.caffeinated.entity.effect.CaffeineStatusEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModStatusEffects {
    public static final StatusEffect CAFFEINE = register("caffeine", StatusEffectCategory.BENEFICIAL, 4927777);

    private static StatusEffect register(String id, StatusEffectCategory category, int i){
        return Registry.register(Registries.STATUS_EFFECT, new Identifier(Caffeinated.MODID, id), new CaffeineStatusEffect(category, i));
    }

    public static void register() {
    }
}
