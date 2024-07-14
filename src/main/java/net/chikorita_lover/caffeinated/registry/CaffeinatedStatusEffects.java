package net.chikorita_lover.caffeinated.registry;

import net.chikorita_lover.caffeinated.Caffeinated;
import net.chikorita_lover.caffeinated.entity.effect.CaffeineStatusEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;

public class CaffeinatedStatusEffects {
    public static final RegistryEntry<StatusEffect> CAFFEINE = register("caffeine", new CaffeineStatusEffect(StatusEffectCategory.BENEFICIAL, 0x4B3121));

    public static void register() {
    }

    private static RegistryEntry<StatusEffect> register(String id, StatusEffect effect) {
        return Registry.registerReference(Registries.STATUS_EFFECT, Caffeinated.of(id), effect);
    }
}
