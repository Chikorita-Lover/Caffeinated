package net.chikorita_lover.caffeinated.registry;

import net.chikorita_lover.caffeinated.Caffeinated;
import net.chikorita_lover.caffeinated.entity.effect.CaffeineStatusEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class CaffeinatedStatusEffects {
    public static final RegistryEntry<StatusEffect> CAFFEINE = register("caffeine", StatusEffectCategory.BENEFICIAL, 0x4B3121);

    private static RegistryEntry<StatusEffect> register(String id, StatusEffectCategory category, int i) {
        return Registry.registerReference(Registries.STATUS_EFFECT, new Identifier(Caffeinated.MODID, id), new CaffeineStatusEffect(category, i));
    }

    public static void register() {
    }
}
