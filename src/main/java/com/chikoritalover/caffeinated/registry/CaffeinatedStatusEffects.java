package com.chikoritalover.caffeinated.registry;

import com.chikoritalover.caffeinated.Caffeinated;
import com.chikoritalover.caffeinated.entity.effect.CaffeineStatusEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class CaffeinatedStatusEffects {
    public static final StatusEffect CAFFEINE = register("caffeine", new CaffeineStatusEffect(StatusEffectCategory.BENEFICIAL, 0x4B3121));

    private static StatusEffect register(String id, StatusEffect effect) {
        return Registry.register(Registries.STATUS_EFFECT, new Identifier(Caffeinated.MODID, id), effect);
    }

    public static void register() {
    }
}
